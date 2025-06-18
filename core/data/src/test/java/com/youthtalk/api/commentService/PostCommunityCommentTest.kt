package com.youthtalk.api.commentService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.commentService.json.postEmptyContentIdJson
import com.youthtalk.api.commentService.json.postEmptyPostsJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.response.postCommentSuccessJson
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

class PostCommunityCommentTest {

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
    fun givenCommunityComment_whenPost_thenWorksFine() = runBlocking {
        // given
        val requestBody = """
            {
                "postId" : 102,
                "content" : "게시글댓글내용"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(postCommentSuccessJson)
        )

        // when
        val response = sut.postPostAddComment(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/comments", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("댓글을 성공적으로 등록했습니다.", response.message)
        assertEquals("S06", response.code)
        assertNotNull(response.data?.commentId)
    }

    @Test
    fun givenEmptyPostsId_whenPost_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = postEmptyContentIdJson
        val requestBody = """
        {
            "content" : "게시글댓글내용"
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
                sut.postPostAddComment(requestBody)
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
    fun givenEmptyContent_whenPost_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = postEmptyContentIdJson
        val requestBody = """
        {
            "postId" : 102
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
                sut.postPostAddComment(requestBody)
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
    fun givenNotFoundPosts_whenPost_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = postEmptyPostsJson
        val requestBody = """
            {
                "postId" : 102,
                "content" : "게시글댓글내용"
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
                sut.postPostAddComment(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<String>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("해당 게시글을 찾을 수 없습니다.", errorResponse.message)
        assertEquals("PS01", errorResponse.code)
        assertNull(errorResponse.data)
    }
}
