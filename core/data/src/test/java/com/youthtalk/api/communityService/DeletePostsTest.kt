package com.youthtalk.api.communityService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.response.commonSuccessJson
import com.youthtalk.api.response.forbiddenJson
import com.youthtalk.api.response.notFoundPostsJson
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
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_OK

class DeletePostsTest {

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
    fun givenPostId_whenDelete_thenWorksFine() = runBlocking {
        // given
        val postId = 8L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(commonSuccessJson)
        )

        // when
        val response = sut.deletePost(postId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        // then
        assertEquals("/api/v1/posts/$postId", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNull(response.data)
    }

    @Test
    fun givenNonExistPostId_whenDelete_thenThrows400Exception() = runBlocking {
        // given
        val postId = 3L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_BAD_REQUEST)
                .setBody(notFoundPostsJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.deletePost(postId)
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
    fun givenForbiddenUsersPost_whenDelete_thenThrows403Exception() = runBlocking {
        // given
        val postId = 7L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_FORBIDDEN)
                .setBody(forbiddenJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.deletePost(postId)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Unit>>(errorBody!!)
        assertEquals(403, errorResponse.status)
        assertEquals("해당 게시글에 대한 권한이 없습니다.", errorResponse.message)
        assertEquals("PS02", errorResponse.code)
        assertNull(errorResponse.data)
    }
}
