package com.youthtalk.api.loginService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.data.LoginService
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.MemberId
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import com.youthtalk.dto.login.LoginRequest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test
import retrofit2.HttpException

class LoginTest {

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
    fun givenKakaoLoginInfo_whenRequestLogin_thenReturnsMemberId() = runBlocking {
        // given
        val loginRequest = LoginRequest(socialType = "kakao", socialId = "1")

        val responseJson = """
            {"status":200,"message":"success","code":"S01","data":{"memberId":1}}
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
        val response = sut.postLogin(loginRequest.toRequestBody())
        val recordedRequest = mockWebServer.takeRequest()

        // then
        // 요청 검증
        assertEquals("/api/v1/login", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("success", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
        assertEquals(1L, response.data?.memberId)
    }

    @Test
    fun givenKakaoLoginInfo_whenRequestLogin_thenReturnsAuthTokens() = runBlocking {
        // given
        val accessToken = "test-access-token"
        val refreshToken = "test-refresh-token"

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader("Authorization", accessToken)
                .addHeader("Authorization-refresh", refreshToken)
        )

        val requestBody = """
            {
                "socialType": "kakao",
                "socialId": "1"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(mockWebServer.url("/api/v1/login"))
            .post(requestBody)
            .build()

        // when
        val response = okHttpClient.newCall(request).execute()

        // then
        assertEquals(200, response.code)
        assertEquals(accessToken, response.header("Authorization"))
        assertEquals(refreshToken, response.header("Authorization-refresh"))
    }

    @Test
    fun givenInvalidSocialId_whenRequestLogin_thenThrows401Exception() = runBlocking {
        // given
        val loginRequest = LoginRequest(socialType = "kakao", socialId = "INVALID_SOCIAL_ID")

        val responseJson = """
            {"status":401,"message":"회원이 아닙니다","code":"M01","data":null}
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(401)
                .setBody(responseJson)
        )

        // when & then
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postLogin(loginRequest.toRequestBody())
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)
        val errorResponse = Json.decodeFromString<CommonResponse<MemberId>>(errorBody!!)
        assertEquals(401, errorResponse.status)
        assertEquals("회원이 아닙니다", errorResponse.message)
        assertEquals("M01", errorResponse.code)
        assertEquals(null, errorResponse.data)
    }
}
