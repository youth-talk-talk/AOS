package com.youthtalk.api.commentService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.commentService.json.getIncorrectPolicyIdCommentJson
import com.youthtalk.api.commentService.json.getPolicyCommentJson
import com.youthtalk.api.commentService.json.getPolicyEmptyCommentJson
import com.youthtalk.api.commentService.json.getPolicyLeaveUserCommentJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.response.notFoundPolicyJson
import com.youthtalk.data.CommentService
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class GetPolicyCommentTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: CommentService

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

        sut = retrofit.create(CommentService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenPolicyId_whenGetComment_thenReturnsPolicyComment() = runBlocking {
        // given
        val policyId = 1L
        val responseJson = getPolicyCommentJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getPolicyComment(policyId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/$policyId/comments", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        assertNotNull(response.data)
    }

    @Test
    fun givenPolicyId_whenGetComment_thenReturnsEmptyPolicyComment() = runBlocking {
        // given
        val policyId = 1L
        val responseJson = getPolicyEmptyCommentJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getPolicyComment(policyId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/$policyId/comments", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("해당하는 댓글이 없습니다.", response.message)
        assertEquals("S09", response.code)

        assertNull(response.data)
    }

    @Test
    fun givenPolicyId_whenGetComment_thenReturnsLeaveUserComment() = runBlocking {
        // given
        val policyId = 1L
        val responseJson = getPolicyLeaveUserCommentJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getPolicyComment(policyId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/policies/$policyId/comments", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        assertTrue(response.data?.comments?.any { it.writerId == -1L } ?: false)
        assertTrue(response.data?.comments?.any { it.nickname == "알 수 없음" } ?: false)
    }

    @Test
    fun givenNonExistPolicyId_whenGetComment_thenThrows400Exception() = runBlocking {
        // given
        val policyId = -1L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(notFoundPolicyJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.getPolicyComment(policyId)
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
    fun givenIncorrectPolicyId_whenGetComment_thenThrows400Exception() {
        // given
        val policyId = 1231321L
        val responseJson = getIncorrectPolicyIdCommentJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.getPolicyComment(policyId)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<String>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertNull(errorResponse.data)
    }
}
