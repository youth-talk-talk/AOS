package com.youthtalk.api.policyService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.policyService.json.optionsPoliciesJson
import com.youthtalk.api.policyService.json.searchPolicyRequestJson
import com.youthtalk.data.PolicyService
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
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class OptionalSearchPolicyTest {

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
    fun givenPolicyOption_whenPostPolicy_thenReturnsPolicies() = runBlocking {
        // given
        val requestBody = searchPolicyRequestJson.toRequestBody("application/json".toMediaType())
        val responseJson = optionsPoliciesJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postSpecPolicies(requestBody, page = 0, size = 10)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/search?sort=RECENT&page=0&size=10", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("정책 조회에 성공하였습니다.", response.message)
        assertEquals("S04", response.code)
    }

    @Test
    fun givenEmptyPolicyOption_whenPostPolicy_thenReturnsPolicies() = runBlocking {
        // given
        val requestBody = """{}""".toRequestBody("application/json".toMediaType())
        val responseJson = optionsPoliciesJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postSpecPolicies(requestBody, page = 0, size = 10)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/search?sort=RECENT&page=0&size=10", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("정책 조회에 성공하였습니다.", response.message)
        assertEquals("S04", response.code)
    }

    @Test
    fun givenNonExistPolicyOption_whenPostPolicy_thenReturnsEmptyData() = runBlocking {
        // given
        val requestBody = searchPolicyRequestJson.toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                    {
                      "status": 200,
                      "message": "조건에 맞는 정책 결과가 없습니다",
                      "code": "S05",
                      "data": {
                        "totalCount": 0,
                        "policyList": []
                      }
                    }
                    """.trimIndent()
                )
        )

        // when
        val response = sut.postSpecPolicies(requestBody, page = 0, size = 10)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/search?sort=RECENT&page=0&size=10", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("조건에 맞는 정책 결과가 없습니다", response.message)
        assertEquals("S05", response.code)

        val policies = response.data ?: return@runBlocking fail("정책들은 null 값이 되면 안됨")
        assertEquals(0, policies.policyList.size)
    }

    @Test
    fun givenIncorrectRequestBody_whenPostPolicy_thenThrows400Exception() = runBlocking {
        // given
        val requestBody = """
            "keyword":"asdasd  "
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(
                    """
                       {
                         "status": 400,
                         "message": "검색어(키워드)가 올바르지 않습니다.",
                         "code": "PC03",
                         "data": null
                       }
                    """.trimIndent()
                )
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postSpecPolicies(requestBody, page = 0, size = 10)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<String>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("검색어(키워드)가 올바르지 않습니다.", errorResponse.message)
        assertEquals("PC03", errorResponse.code)
        assertNull(errorResponse.data)
    }
}
