package com.youthtalk.api.commentService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.commentService.json.deleteSuccessJson
import com.youthtalk.api.commentService.json.patchCommentIdEmptyErrorJson
import com.youthtalk.api.commentService.json.patchContentEmptyErrorJson
import com.youthtalk.api.commentService.json.patchSuccessJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.response.invalidParameterJson
import com.youthtalk.api.response.notFoundCommentJson
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

class PatchCommentTest {

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
    fun given_whenPatch_thenWorksFine() = runBlocking {
        // given
        val responseJson = patchSuccessJson
        val requestBody = """
            {
                "commentId" : 382,
                "content" : "댓글내용수정1"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.patchComment(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/comments", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("댓글을 성공적으로 수정했습니다.", response.message)
        assertEquals("S07", response.code)
        assertNull(response.data)
    }

    @Test
    fun givenNoCommentId_whenPatch_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = patchCommentIdEmptyErrorJson
        val requestBody = """
            {
                "content" : "댓글내용수정1" // commentId 없음
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
                sut.patchComment(requestBody)
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
    fun givenNoContent_whenPatch_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = patchContentEmptyErrorJson
        val requestBody = """
            {
                "commentId" : 382
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
                sut.patchComment(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("must not be blank", errorResponse.data?.get("messages")?.get(0))
    }

    @Test
    fun givenNoExistComment_whenPatch_thenThrows400Exception() = runBlocking {
        // given
        val requestBody = """
            {
                "commentId" : 7777,
                "content" : "댓글내용수정1"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(notFoundCommentJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.patchComment(requestBody)
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

    @Test
    fun givenCommentId_whenDelete_thenWorksFine() = runBlocking {
        // given
        val commentId = 1L
        val responseJson = deleteSuccessJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postDeleteComment(commentId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/comments/$commentId", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("댓글을 성공적으로 삭제했습니다.", response.message)
        assertEquals("S08", response.code)
        assertNull(response.data)
    }

    @Test
    fun givenIncorrectCommentId_whenDeleting_thenThrows400Exception() = runBlocking {
        // given
        val commentId = 123121232L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(invalidParameterJson)
        )
        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postDeleteComment(commentId)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Unit>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertNull(errorResponse.data)
    }

    @Test
    fun givenNotFoundCommentId_whenDeleting_thenThrows400Exception() = runBlocking {
        // given
        val commentId = 123121232L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(notFoundCommentJson)
        )
        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postDeleteComment(commentId)
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
