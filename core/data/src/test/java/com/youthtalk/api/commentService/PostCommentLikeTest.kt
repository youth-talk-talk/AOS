package com.youthtalk.api.commentService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.commentService.json.postLikeNoCommentIdErrorJson
import com.youthtalk.api.commentService.json.postLikeSuccessJson
import com.youthtalk.api.commentService.json.postNotFoundCommentIdErrorJson
import com.youthtalk.api.commentService.json.postUnLikeSuccessJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.data.CommentService
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

class PostCommentLikeTest {

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
    fun givenLikeTrue_whenPostLike_thenWorksFine() = runBlocking {
        // given
        val responseJson = postLikeSuccessJson
        val requestBody = """
            {
                "commentId" : 144,
                "isSetLiked" : true
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postLikes(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/comments/likes", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("좋아요 등록이 완료되었습니다.", response.message)
        assertEquals("S10", response.code)
        assertNull(response.data)
    }

    @Test
    fun givenLikeFalse_whenPostLike_thenWorksFine() = runBlocking {
        // given
        val responseJson = postUnLikeSuccessJson
        val requestBody = """
            {
                "commentId" : 144,
                "isSetLiked" : false
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postLikes(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/comments/likes", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("좋아요 해제가 완료되었습니다.", response.message)
        assertEquals("S11", response.code)
        assertNull(response.data)
    }

    @Test
    fun givenEmptyCommentId_whenPostLike_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = postLikeNoCommentIdErrorJson
        val requestBody = """
            {
                "isSetLiked" : false
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
                sut.postLikes(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("must not be null", errorResponse.data?.get("messages")?.get(0))
    }

    @Test
    fun givenNotFoundCommentId_whenPostLike_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = postNotFoundCommentIdErrorJson
        val requestBody = """
            {
                "commentId" : 7777
                "isSetLiked" : false
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
                sut.postLikes(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Unit>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("해당 댓글을 찾을 수 없습니다.", errorResponse.message)
        assertEquals("C01", errorResponse.code)
        assertNull(errorResponse.data)
    }
}
