package com.youthtalk.api.policyService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.policyService.json.homePolicyJson
import com.youthtalk.api.policyService.json.newPolicyJson
import com.youthtalk.api.policyService.json.recentlyViewPoliciesJson
import com.youthtalk.api.policyService.json.scrapedPoliciesJson
import com.youthtalk.data.PolicyService
import com.youthtalk.model.policy.Policy
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class GetPoliciesTest {

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
    fun given_whenGetHomePolicies_thenReturnsPolicies() = runBlocking {
        // given
        val responseJson = homePolicyJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getHome()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/home?sort=RECENT", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun given_whenGetNewPolicies_thenReturnsNewPolicies() = runBlocking {
        // given
        val responseJson = newPolicyJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getNewPolicies()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/home/new-policies?sort=RECENT", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        val policies = response.data ?: return@runBlocking fail("정책들은 null 값이 되면 안됨")
        assertEquals(
            emptyList<Policy>(),
            policies.all + policies.job + policies.life + policies.dwelling + policies.education + policies.participation
        )
    }

    @Test
    fun given_whenGetScrapedPolicies_thenReturnsScrapedPolicies() = runBlocking {
        // given
        val responseJson = scrapedPoliciesJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getScrapPolicies(page = 0, size = 10)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/scrap?page=0&size=10", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        val policies = response.data ?: return@runBlocking fail("정책들은 null 값이 되면 안됨")
        assertTrue(policies.all { it.scrap })
    }

    @Test
    fun given_whenGetRecentViewPolicies_thenReturnsRecentViewPolicies() = runBlocking {
        // given
        val responseJson = recentlyViewPoliciesJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getRecentlyViewPolicies()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/recent-view", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        assertNotNull(response.data)
    }
}
