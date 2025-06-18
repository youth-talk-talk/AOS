package com.youthtalk.api.communityService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.communityService.json.patchEmptyContentFailJson
import com.youthtalk.api.communityService.json.patchEmptyContentRequestBody
import com.youthtalk.api.communityService.json.patchRequestBody
import com.youthtalk.api.communityService.json.patchSuccessJson
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.response.forbiddenJson
import com.youthtalk.api.response.notFoundPolicyJson
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
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_OK

class PatchPostsTest {

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
    fun givenRequestBody_whenPatch_thenWorksFine() = runBlocking {
        // given
        val postId = 50L
        val responseJson = patchSuccessJson
        val requestBody = patchRequestBody.toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(responseJson)
        )

        // when
        val response = sut.postModifyPost(postId, requestBody)
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
    fun givenEmptyContent_whenPatch_thenThrows400Exception() = runBlocking {
        // given
        val postId = 37L
        val responseJson = patchEmptyContentFailJson
        val requestBody = patchEmptyContentRequestBody.toRequestBody("application/json".toMediaType())

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_BAD_REQUEST)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postModifyPost(postId, requestBody)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<Map<String, List<String>>>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("유효하지 않은 값을 입력하였습니다.", errorResponse.message)
        assertEquals("F01", errorResponse.code)
        assertEquals("게시글 본문은 필수값입니다.", errorResponse.data?.get("messages")?.get(0))
    }

    @Test
    fun givenNotFoundPolicy_whenPatch_thenThrows400Exception() = runBlocking {
        // given
        val postId = 37L

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_BAD_REQUEST)
                .setBody(notFoundPolicyJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postModifyPost(postId, """""".toRequestBody("application/json".toMediaType()))
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
    fun givenForbiddenUsersPost_whenPatch_thenThrows403Exception() = runBlocking {
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
                sut.postModifyPost(postId, """""".toRequestBody("application/json".toMediaType()))
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
