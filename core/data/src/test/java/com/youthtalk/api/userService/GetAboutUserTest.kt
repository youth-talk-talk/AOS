package com.youthtalk.api.userService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.response.commentEmptySuccessJson
import com.youthtalk.api.userService.json.myCommentInfo
import com.youthtalk.api.userService.json.userInfoJson
import com.youthtalk.data.UserService
import kotlinx.coroutines.runBlocking
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

class GetAboutUserTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: UserService

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

        sut = retrofit.create(UserService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenUserToken_whenGetUserInfo_thenReturnsUserInfo() = runBlocking {
        // given
        val responseJson = userInfoJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getUser()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/me", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun given_whenGetUserInfo_thenThrows403Exception() = runBlocking {
        // given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.getUser()
            }
        }

        // then
        val body = exception.response()?.body()
        assertNull(body)
    }

    @Test
    fun given_whenGetMyComments_thenReturnsComments() = runBlocking {
        // given
        val responseJson = myCommentInfo

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getMyComments()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/me/comments", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun given_whenGetMyComments_thenReturnsDataNull() = runBlocking {
        // given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(commentEmptySuccessJson)
        )

        // when
        val response = sut.getMyComments()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/me/comments", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("해당하는 댓글이 없습니다.", response.message)
        assertEquals("S09", response.code)
        assertNull(response.data)
    }

    @Test
    fun given_whenGetLikeComments_thenReturnsComments() = runBlocking {
        // given
        val responseJson = myCommentInfo

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getLikeComments()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/me/comments/likes", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun given_whenGetLikeComments_thenReturnsDataNull() = runBlocking {
        // given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(commentEmptySuccessJson)
        )

        // when
        val response = sut.getLikeComments()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/me/comments/likes", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("해당하는 댓글이 없습니다.", response.message)
        assertEquals("S09", response.code)
        assertNull(response.data)
    }
}
