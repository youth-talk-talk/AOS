package com.youthtalk.api.loginService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.data.LoginService
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.login.SignRequest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit

class SignUpTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: LoginService
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiTestUtils.httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(ApiTestUtils.converterFactory)
            .build()

        sut = retrofit.create(LoginService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenKakaoSignUp_whenRequestSignUp_thenReturnsMemberId() = runBlocking {
        // given
        val signUpRequest = SignRequest(socialId = "123456", socialType = "kakao", nickname = "압도적도적", region = "서울")

        val responseJson = """
            {"status":200,"message":"요청에 성공하였습니다.","code":"S01","data":21}
        """.trimIndent()

        val accessToken = "test-access-token"
        val refreshToken = "test-refresh-token"

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
                .addHeader("Authorization", accessToken)
                .addHeader("Authorization-refresh", refreshToken)
        )

        // when
        val response = sut.postSignUp(signUpRequest.toRequestBody())
        val recordedRequest = mockWebServer.takeRequest()

        // then
        // 요청 검증
        assertEquals("/api/v1/signUp", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertEquals(21, response.data)
    }

    @Test
    fun givenSizeOverNickname_whenRequestSignUp_thenThrows400Exception() = runBlocking {
        // given
        val signUpRequest = SignRequest(socialId = "123456", socialType = "kakao", nickname = "8자초과하는닉네임", region = "서울")

        val responseJson = """
            {
            "status": 400,
            "message": "유효하지 않은 값을 입력하였습니다.",
            "code": "F01",
            "data": {
             "messages": [
                "닉네임 길이는 1자 이상 8자 이하여야 합니다."
             ]
           }
        }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postSignUp(signUpRequest.toRequestBody())
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)
        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("닉네임 길이는 1자 이상 8자 이하여야 합니다.", errorResponse.data?.get("messages")?.get(0) ?: "")
    }

    @Test
    fun givenNowAllowedNickname_whenRequestSignUp_thenThrows400Exception() = runBlocking {
        // given
        val signUpRequest = SignRequest(socialId = "123456", socialType = "kakao", nickname = "member10!", region = "서울")

        val responseJson = """
            {"status": 400, "message": "유효하지 않은 값을 입력하였습니다.", "code": "F01",
            "data": {
             "messages": [
                "닉네임은 한글과 영어(대소문자) 및 숫자만 가능하며, 공백과 특수문자는 사용할 수 없습니다."
             ]
           }
        }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postSignUp(signUpRequest.toRequestBody())
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)
        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("닉네임은 한글과 영어(대소문자) 및 숫자만 가능하며, 공백과 특수문자는 사용할 수 없습니다.", errorResponse.data?.get("messages")?.get(0) ?: "")
    }

    @Test
    fun givenNowAllowedNicknameAndSizeOverNickName_whenRequestSignUp_thenThrows400Exception() = runBlocking {
        // given
        val signUpRequest = SignRequest(socialId = "123456", socialType = "kakao", nickname = "member10!@#", region = "서울")

        val responseJson = """
            {"status": 400, "message": "유효하지 않은 값을 입력하였습니다.", "code": "F01",
            "data": {
             "messages": [
              "닉네임은 한글과 영어(대소문자) 및 숫자만 가능하며, 공백과 특수문자는 사용할 수 없습니다.",
              "닉네임 길이는 1자 이상 8자 이하여야 합니다."
             ]
           }
        }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postSignUp(signUpRequest.toRequestBody())
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)
        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("닉네임은 한글과 영어(대소문자) 및 숫자만 가능하며, 공백과 특수문자는 사용할 수 없습니다.", errorResponse.data?.get("messages")?.get(0) ?: "")
        assertEquals("닉네임 길이는 1자 이상 8자 이하여야 합니다.", errorResponse.data?.get("messages")?.get(1) ?: "")
    }

    @Test
    fun givenInvalidRegion_whenRequestSignUp_thenThrows400Exception() = runBlocking {
        // given
        val signUpRequest = SignRequest(socialId = "123456", socialType = "kakao", nickname = "압도적도적", region = "없는 지역")

        val responseJson = """
            {"status": 400, "message": "유효하지 않은 값을 입력하였습니다.", "code": "F01",
            "data": {
             "messages": [
              "지역이 유효하지 않습니다."
             ]
           }
        }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postSignUp(signUpRequest.toRequestBody())
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)
        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("지역이 유효하지 않습니다.", errorResponse.data?.get("messages")?.get(0) ?: "")
    }
}
