package com.youthtalk.api.communityService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.communityService.json.getLeaveUserPostJson
import com.youthtalk.api.communityService.json.postReviewSuccessJson
import com.youthtalk.api.communityService.json.postSuccessJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.response.notFoundPostsJson
import com.youthtalk.api.response.reportedPostsJson
import com.youthtalk.api.response.reportedUserJson
import com.youthtalk.data.CommunityService
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
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_OK

class GetPostsDetailTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: CommunityService

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

        sut = retrofit.create(CommunityService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenPostId_whenGet_thenReturnsPostsDetail() = runBlocking {
        // given
        val postId = 51L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(postSuccessJson)
        )

        // when
        val response = sut.getPostDetail(postId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/$postId", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertEquals(postId, response.data?.postId)
    }

    @Test
    fun givenPostId_whenGet_thenReturnsReviewDetail() = runBlocking {
        // given
        val postId = 50L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(postReviewSuccessJson)
        )

        // when
        val response = sut.getPostDetail(postId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/$postId", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertEquals(postId, response.data?.postId)
    }

    @Test
    fun givenPostId_whenGet_thenThrows400Exception() = runBlocking {
        // given
        val noExistPostId = 8L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_BAD_REQUEST)
                .setBody(notFoundPostsJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.getPostDetail(noExistPostId)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Unit>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("해당 게시글을 찾을 수 없습니다.", errorResponse.message)
        assertEquals("PS01", errorResponse.code)
        assertNull(errorResponse.data)
    }

    @Test
    fun givenLeaveUserPostId_whenGet_thenReturnsWriterIdNull() = runBlocking {
        // given
        val postId = 415L
        val responseJson = getLeaveUserPostJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(responseJson)
        )

        // when
        val response = sut.getPostDetail(postId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/$postId", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNull(response.data?.writerId)
    }

    @Test
    fun givenReportedPostId_whenGet_thenThrows400Exception() = runBlocking {
        // given
        val postId = 126L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_BAD_REQUEST)
                .setBody(reportedPostsJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.getPostDetail(postId)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Unit>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("신고한 게시글은 조회할 수 없습니다.", errorResponse.message)
        assertEquals("PS03", errorResponse.code)
        assertNull(errorResponse.data)
    }

    @Test
    fun givenReportedUserPostId_whenGet_thenThrows400Exception() = runBlocking {
        // given
        val postId = 123L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_BAD_REQUEST)
                .setBody(reportedUserJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.getPostDetail(postId)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Unit>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("차단한 유저의 게시글은 조회할 수 없습니다.", errorResponse.message)
        assertEquals("PS04", errorResponse.code)
        assertNull(errorResponse.data)
    }
}
