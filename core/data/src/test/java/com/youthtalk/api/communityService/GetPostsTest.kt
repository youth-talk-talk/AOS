package com.youthtalk.api.communityService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.communityService.json.getSearchJson
import com.youthtalk.api.communityService.json.getTotalPostsJson
import com.youthtalk.api.communityService.json.getTotalReviewsJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.data.CommunityService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection.HTTP_OK

class GetPostsTest {

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
    fun given_whenGetTotalPosts_thenWorksFine() = runBlocking {
        // given
        val page = 0
        val size = 10
        val responseJson = getTotalPostsJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(responseJson)
        )

        // when
        val response = sut.getPosts(page, size)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/post?page=$page&size=$size", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun givenCategory_whenGetTotalReviews_thenWorksFine() = runBlocking {
        // given
        val page = 0
        val size = 10
        val categories = listOf("JOB")
        val responseJson = getTotalReviewsJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(responseJson)
        )

        // when
        val response = sut.postReviewPosts(page, size, categories)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/review?page=$page&size=$size&categories=${categories[0]}", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun given_whenGetTotalReviews_thenWorksFine() = runBlocking {
        // given
        val page = 0
        val size = 10
        val responseJson = getTotalReviewsJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(responseJson)
        )

        // when
        val response = sut.postReviewPosts(page, size, listOf())
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/review?page=$page&size=$size", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun givenKeywordAndTypePost_whenGetKeywordSearch_thenWorksFine() = runBlocking {
        // given
        val keyword = "hello"
        val type = "post"
        val page = 0
        val size = 10
        val responseJson = getSearchJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(responseJson)
        )

        // when
        val response = sut.getSearchPosts(keyword, type, page, size)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/keyword?keyword=$keyword&type=$type&page=$page&size=$size", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun givenKeywordAndTypeReview_whenGetKeywordSearch_thenWorksFine() = runBlocking {
        // given
        val keyword = "hello"
        val type = "review"
        val page = 0
        val size = 10
        val responseJson = getSearchJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(responseJson)
        )

        // when
        val response = sut.getSearchPosts(keyword, type, page, size)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/keyword?keyword=$keyword&type=$type&page=$page&size=$size", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun given_whenGetMyPost_thenWorksFine() = runBlocking {
        // given
        val page = 0
        val size = 10
        val responseJson = getSearchJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(responseJson)
        )

        // when
        val response = sut.getMyPosts(page, size)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/me?page=$page&size=$size", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }

    @Test
    fun given_whenGetScrapPost_thenWorksFine() = runBlocking {
        // given
        val page = 0
        val size = 10
        val responseJson = getSearchJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(responseJson)
        )

        // when
        val response = sut.getScrapPosts(page, size)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts/scrap?page=$page&size=$size", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNotNull(response.data)
    }
}
