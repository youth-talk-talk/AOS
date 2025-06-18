package com.youthtalk.api.communityService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.communityService.json.postDataJson
import com.youthtalk.api.communityService.json.postErrorWithoutParameterJson
import com.youthtalk.api.communityService.json.postReviewDataJson
import com.youthtalk.api.communityService.json.postReviewSuccessJson
import com.youthtalk.api.communityService.json.postSuccessJson
import com.youthtalk.api.communityService.json.postWithoutParameterJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.data.CommunityService
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

class PostPostsTest {

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
    fun givenPosts_whenPost_thenWorksFine() = runBlocking {
        // given
        val responseJson = postSuccessJson
        val requestBody = postDataJson.toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postCreate(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        assertEquals("post", response.data?.postType)
    }

    @Test
    fun givenPostsReview_whenPost_thenWorksFine() = runBlocking {
        // given
        val responseJson = postReviewSuccessJson
        val requestBody = postReviewDataJson.toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postCreate(requestBody)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/posts", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        assertEquals("review", response.data?.postType)
    }

    @Test
    fun givenNoExistPolicyId_whenPost_thenThrows400Exception() = runBlocking {
        // given

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(
                    """
                        {
                          "status": 400,
                          "message": "해당 정책을 찾을 수 없습니다.",
                          "code": "PC01",
                          "data": null
                        }
                    """.trimIndent()
                )
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postCreate("""""".toRequestBody("application/json".toMediaType()))
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Unit>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("해당 정책을 찾을 수 없습니다.", errorResponse.message)
        assertEquals("PC01", errorResponse.code)
        assertNull(errorResponse.data)
    }

    @Test
    fun givenIncorrectParameter_whenPost_thenThrows400Exception() = runBlocking {
        // given
        val responseJson = postErrorWithoutParameterJson
        val requestBody = postWithoutParameterJson.toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postCreate(requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("Content list must contain at least one item", errorResponse.data?.get("messages")?.get(0))
    }
}
