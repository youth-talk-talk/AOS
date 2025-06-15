package com.youthtalk.api.policyService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.data.PolicyService
import com.youthtalk.dto.CommonResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
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

class ScrapPolicyTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: PolicyService

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

        sut = retrofit.create(PolicyService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenPolicyNum_whenScrapPolicy_thenWorksFine() = runBlocking {
        // given
        val policyNum = 1L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                        {
                          "status": 200,
                          "message": "스크랩에 성공하였습니다.",
                          "code": "S02",
                          "data": null
                        }
                    """.trimIndent()
                )
        )

        // when
        val response = sut.postPolicyScrap(policyNum)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/$policyNum/scrap", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("스크랩에 성공하였습니다.", response.message)
        assertEquals("S02", response.code)
        assertNull(response.data)
    }

    @Test
    fun givenPolicyNum_whenCancelScrapPolicy_thenWorksFine() = runBlocking {
        // given
        val policyNum = 1L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                        {
                          "status": 200,
                          "message": "스크랩을 취소했습니다.",
                          "code": "S02",
                          "data": null
                        }
                    """.trimIndent()
                )
        )

        // when
        val response = sut.postPolicyScrap(policyNum)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/$policyNum/scrap", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("스크랩을 취소했습니다.", response.message)
        assertEquals("S02", response.code)
        assertNull(response.data)
    }

    @Test
    fun givenNotExistPolicyNum_whenScrapPolicy_thenThrows400Exception() = runBlocking {
        // given
        val policyNum = 1L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(
                    """
                        {
                          "status": 400,
                          "message": "해당 정책을 찾을 수 없습니다.",
                          "code": "PC01",
                          "data": null
                        }
                    """.trimIndent()
                )
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postPolicyScrap(policyNum)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<String>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("해당 정책을 찾을 수 없습니다.", errorResponse.message)
        assertEquals("PC01", errorResponse.code)
        assertNull(errorResponse.data)
    }

    @Test
    fun given_whenDeleteScrap_thenWorksFine() = runBlocking {
        // given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                    {
                      "status": 200,
                      "message": "요청에 성공하였습니다.",
                      "code": "S01",
                      "data": "success"
                    }
                    """.trimIndent()
                )
        )

        // when
        val response = sut.deleteAllRecentlyViewPolicies()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/recent-view", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertEquals(response.data, "success")
    }
}
