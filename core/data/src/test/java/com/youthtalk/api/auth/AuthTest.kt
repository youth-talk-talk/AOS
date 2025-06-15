package com.youthtalk.api.auth

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.interceptor.EmptyAuthInterceptor
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.dto.CommonResponse
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenAuthInterceptorWithAuthToken_whenRequest_thenWorksFine() {
        // given
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TestAuthInterceptor())
            .addInterceptor(ApiTestUtils.httpLoggingInterceptor)
            .build()

        val requestBody = """
            {
              "status": 200,
              "message": "요청에 성공하였습니다.",
              "code": "S01",
              "data": "something"
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(requestBody)
        )

        val request = Request.Builder()
            .url(mockWebServer.url(""))
            .get()
            .build()

        // when
        val response = okHttpClient.newCall(request).execute()

        // then
        assertEquals(200, response.code)

        val responseBodyString = response.body?.string() ?: error("response body is null")
        val responseBody = Json.decodeFromString<CommonResponse<String>>(responseBodyString)
        assertEquals(200, responseBody.status)
        assertEquals("요청에 성공하였습니다.", responseBody.message)
        assertEquals("S01", responseBody.code)
    }

    @Test
    fun givenEmptyAuthInterceptor_whenRequest_thenThrows401Exception() {
        // given
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(EmptyAuthInterceptor())
            .addInterceptor(ApiTestUtils.httpLoggingInterceptor)
            .build()

        val requestBody = """
            {
              "status": 401,
              "message": "유효하지 않은 엑세스 토큰입니다.",
              "code": "T01",
              "data": null
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(401)
                .setBody(requestBody)
        )

        val request = Request.Builder()
            .url(mockWebServer.url(""))
            .get()
            .build()

        // when
        val response = okHttpClient.newCall(request).execute()

        // then
        assertEquals(401, response.code)

        val responseBodyString = response.body?.string() ?: error("response body is null")
        val responseBody = Json.decodeFromString<CommonResponse<String>>(responseBodyString)
        assertEquals(401, responseBody.status)
        assertEquals("유효하지 않은 엑세스 토큰입니다.", responseBody.message)
        assertEquals("T01", responseBody.code)
        assertNull(responseBody.data)
    }
}
