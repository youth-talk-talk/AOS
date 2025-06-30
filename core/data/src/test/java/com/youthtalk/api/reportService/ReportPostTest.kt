package com.youthtalk.api.reportService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.reportService.json.alreadyReportedResponseJson
import com.youthtalk.api.reportService.json.myReportedResponseJson
import com.youthtalk.api.response.commonSuccessJson
import com.youthtalk.api.response.notFoundPostsJson
import com.youthtalk.data.ReportService
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
import java.net.HttpURLConnection.HTTP_CONFLICT
import java.net.HttpURLConnection.HTTP_OK

class ReportPostTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: ReportService

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

        sut = retrofit.create(ReportService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenPostId_whenReport_thenWorksFine() = runBlocking {
        // given
        val postId = 72L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(commonSuccessJson)
        )

        // when
        val response = sut.reportPosts(postId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/report/post/$postId", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)
        assertNull(response.data)
    }

    @Test
    fun givenReportedPostId_whenReport_thenThrows409Exception() = runBlocking {
        // given
        val postId = 72L
        val responseJson = alreadyReportedResponseJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_CONFLICT)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.reportPosts(postId)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<String>>(errorBody!!)
        assertEquals(409, errorResponse.status)
        assertEquals("이미 신고한 게시글(또는 댓글)입니다.", errorResponse.message)
        assertEquals("R01", errorResponse.code)
        assertNull(errorResponse.data)
    }

    @Test
    fun givenMyPostId_whenReport_thenThrows400Exception() = runBlocking {
        // given
        val postId = 72L
        val responseJson = myReportedResponseJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_BAD_REQUEST)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.reportPosts(postId)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<String>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("본인의 게시글(또는 댓글)은 신고할 수 없습니다.", errorResponse.message)
        assertEquals("R02", errorResponse.code)
        assertNull(errorResponse.data)
    }

    @Test
    fun givenNoExistPostId_whenReport_thenThrows400Exception() = runBlocking {
        // given
        val postId = 213123L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_BAD_REQUEST)
                .setBody(notFoundPostsJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.reportPosts(postId)
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
}
