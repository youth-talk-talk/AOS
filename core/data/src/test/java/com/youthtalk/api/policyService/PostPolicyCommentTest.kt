package com.youthtalk.api.policyService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.policyService.json.postPolicyCommentSuccessJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.policyService.json.postCommentFailWithoutContentJson
import com.youthtalk.api.policyService.json.postCommentFailWithoutIdJson
import com.youthtalk.api.policyService.json.postCommentFailWithoutPolicyJson
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
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class PostPolicyCommentTest {

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
    fun givenPolicyComment_whenPost_thenWorksFine() = runBlocking {
        // given
        val responseJson = postPolicyCommentSuccessJson
        val requestBody = """
            {
                "policyId" : "R2023081716945",
                "content" : "댓글내용"
            }'
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postAddComment(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/comments", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("댓글을 성공적으로 등록했습니다.", response.message)
        assertEquals("S06", response.code)
        assertEquals(381L, response.data?.commentId)
    }

    @Test
    fun givenEmptyPolicyId_whenPost_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = postCommentFailWithoutIdJson
        val requestBody = """
            {
                "content" : "content"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postAddComment(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("policyId는 필수값입니다.", errorResponse.data?.get("messages")?.get(0))
    }

    @Test
    fun givenEmptyContent_whenPost_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = postCommentFailWithoutContentJson
        val requestBody = """
            {
                "policyId" : "R2023081716945"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postAddComment(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("content는 필수값입니다.", errorResponse.data?.get("messages")?.get(0))
    }

    @Test
    fun givenNonExistPolicyIdComment_whenPost_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = postCommentFailWithoutPolicyJson
        val requestBody = """
            {
                "policyId" : "notPolicyId", // 존재하지 않는 policy
                "content" : "content"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postAddComment(requestBody)
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
}
