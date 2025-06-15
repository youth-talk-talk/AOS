package com.youthtalk.api.policyService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.policyService.json.policyJson
import com.youthtalk.data.PolicyService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetPolicyTest {

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
    fun givenPolicyId_whenGetPolicy_thenReturnsPolicy() = runBlocking {
        // given
        val policyId = 1L
        val responseJson = policyJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getPolicyDetail(policyId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/$policyId", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("정책 조회에 성공하였습니다.", response.message)
        assertEquals("S04", response.code)
        assertNotNull(response.data)
    }
}
