package com.youthtalk.api.announceService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.announceService.json.getAnnounceSuccessJson
import com.youthtalk.api.announceService.json.getNoExistAnnounceErrorJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.data.AnnounceService
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

class GetAnnounceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: AnnounceService

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

        sut = retrofit.create(AnnounceService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenAnnounceId_whenGet_thenWorksFine() = runBlocking {
        // given
        val announceId = 3L
        val responseJson = getAnnounceSuccessJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.getAnnounceDetail(announceId)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/announcements/$announceId", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        assertEquals(announceId, response.data?.id)
    }

    @Test
    fun givenNoExistAnnounceId_whenGet_thenThrows404Exception() = runBlocking {
        // given
        val noExistAnnounceId = 2L
        val responseJson = getNoExistAnnounceErrorJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.getAnnounceDetail(noExistAnnounceId)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Unit>>(errorBody!!)
        assertEquals(404, errorResponse.status)
        assertEquals("해당 공지사항을 찾을 수 없습니다.", errorResponse.message)
        assertEquals("A01", errorResponse.code)
        assertNull(errorResponse.data)
    }
}
