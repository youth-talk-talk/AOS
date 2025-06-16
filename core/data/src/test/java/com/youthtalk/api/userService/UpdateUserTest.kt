package com.youthtalk.api.userService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.userService.json.incorrectNickNameJson
import com.youthtalk.api.userService.json.nicknameAndRegionPatchJson
import com.youthtalk.api.userService.json.nicknamePatchJson
import com.youthtalk.api.userService.json.nonExistRegionJson
import com.youthtalk.api.userService.json.regionPatchJson
import com.youthtalk.api.userService.json.sizeOverAndIncorrectNickNameJson
import com.youthtalk.api.userService.json.sizeOverNicknameJson
import com.youthtalk.data.UserService
import com.youthtalk.dto.CommonResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class UpdateUserTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: UserService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiTestUtils.httpLoggingInterceptor)
            .addInterceptor(TestAuthInterceptor())
            .build()

        val retrofit = createRetrofit(
            baseUrl = mockWebServer.url("/"),
            client = okHttpClient
        )

        sut = retrofit.create(UserService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenNickNameAndRegion_thenPatchUserInfo_thenWorksFine() = runBlocking {
        // given
        val requestBody = """
            {
                "nickname" : "닉네임1",
                "region" : "제주"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())
        val responseJson = nicknameAndRegionPatchJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postUser(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/me", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("회원정보 수정을 완료하였습니다.", response.message)
        assertEquals("S12", response.code)

        assertEquals(response.data?.nickname, "닉네임1")
        assertEquals(response.data?.region, "제주")
    }

    @Test
    fun givenNickName_thenPatchUserInfo_thenWorksFine() = runBlocking {
        // given
        val requestBody = """
            {
                "nickname" : "닉네임2"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())
        val responseJson = nicknamePatchJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postUser(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/me", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("회원정보 수정을 완료하였습니다.", response.message)
        assertEquals("S12", response.code)

        assertEquals(response.data?.nickname, "닉네임2")
    }

    @Test
    fun givenRegion_thenPatchUserInfo_thenWorksFine() = runBlocking {
        // given
        val requestBody = """
            {
                "region" : "부산"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())
        val responseJson = regionPatchJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postUser(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/me", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("회원정보 수정을 완료하였습니다.", response.message)
        assertEquals("S12", response.code)

        assertEquals(response.data?.region, "부산")
    }

    @Test
    fun givenSizeOverNickName_thenPatchUserInfo_thenThrow400Exception() = runBlocking {
        // given
        val requestBody = """
            {
                "nickname" : "길이조건만족안하는닉네임"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())
        val responseJson = sizeOverNicknameJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postUser(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)

        assertEquals("닉네임 길이는 8자 이하입니다.", errorResponse.data?.get("messages")?.get(0))
    }

    @Test
    fun givenIncorrectNickName_thenPatchUserInfo_thenThrows400Exception() = runBlocking {
        // given
        val requestBody = """
            "nickname" : "member1@"
        """.trimIndent().toRequestBody("application/json".toMediaType())
        val responseJson = incorrectNickNameJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postUser(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)

        assertEquals("닉네임은 한글과 영어(대소문자) 및 숫자만 가능하며, 공백과 특수문자는 사용할 수 없습니다.", errorResponse.data?.get("messages")?.get(0))
    }

    @Test
    fun givenSizeOverAndIncorrectNickName_thenPatchUserInfo_thenThrows400Exception() = runBlocking {
        // given
        val requestBody = """
            "nickname" : "memberasdsd1@"
        """.trimIndent().toRequestBody("application/json".toMediaType())
        val responseJson = sizeOverAndIncorrectNickNameJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postUser(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)

        assertEquals("닉네임 길이는 1자 이상 8자 이하여야 합니다.", errorResponse.data?.get("messages")?.get(0))
        assertEquals("닉네임은 한글과 영어(대소문자) 및 숫자만 가능하며, 공백과 특수문자는 사용할 수 없습니다.", errorResponse.data?.get("messages")?.get(1))
    }

    @Test
    fun givenNonExistRegion_thenPatchUserInfo_thenThrows400Exception() = runBlocking {
        // given
        val requestBody = """
            "region" : "없는지역"
        """.trimIndent().toRequestBody("application/json".toMediaType())
        val responseJson = nonExistRegionJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postUser(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)

        assertEquals("지역이 유효하지 않습니다.", errorResponse.data?.get("messages")?.get(0))
    }

    @Test
    fun given_whenDeleteUser_thenWorksFine() = runBlocking {
        // given
        val responseJson = """
            {
              "status": 200,
              "message": "요청에 성공하였습니다.",
              "code": "S01",
              "data": null
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postDeleteUser()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/me", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        assertNull(response.data)
    }

    @Test
    fun givenNotAccessToken_whenDeleteUser_thenThrows403Exception() = runBlocking {
        // given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postDeleteUser()
            }
        }

        // then
        val body = exception.response()?.body()
        assertNull(body)
    }
}
