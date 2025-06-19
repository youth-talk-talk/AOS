package com.youthtalk.api.userService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.api.response.commonSuccessJson
import com.youthtalk.api.userService.json.imageJson
import com.youthtalk.api.userService.json.sizeOverImageJson
import com.youthtalk.data.UserService
import com.youthtalk.dto.CommonResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
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

class UpdateUserImageTest {

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
    fun givenImage_whenPost_thenWorksFine() = runBlocking {
        // given
        val image = MultipartBody.Part.create(
            """
            'image=@"/Users/seulgi/Downloads/test_img.jpeg"'
            """.trimIndent().toRequestBody("application/json".toMediaType())
        )
        val responseJson = imageJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.postUserImage(image)
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/profile", recordedRequest.path)

        // 응답
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        assertNotNull(response.data)
    }

    @Test
    fun givenSizeOverImage_whenPost_thenThrows400Exception() = runBlocking {
        // given
        val image = MultipartBody.Part.create(
            """
            'image=@"/Users/seulgi/Downloads/over_5MB.jpg"'
            """.trimIndent().toRequestBody("application/json".toMediaType())
        )
        val responseJson = sizeOverImageJson

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(responseJson)
        )

        // when
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                sut.postUserImage(image)
            }
        }

        // then
        val errorBody = exception.response()?.errorBody()?.string()
        assertNotNull(errorBody)

        val errorResponse = Json.decodeFromString<CommonResponse<String>>(errorBody!!)
        assertEquals(400, errorResponse.status)
        assertEquals("프로필 이미지는 최대 5MB까지 업로드할 수 있습니다.", errorResponse.message)
        assertEquals("M12", errorResponse.code)

        assertNull(errorResponse.data)
    }

//    @Test
    fun given_whenDelete_thenWorksFine() = runBlocking {
        // given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(commonSuccessJson)
        )

        // when
        val response = sut.deleteUserImage()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/profile", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("요청에 성공하였습니다.", response.message)
        assertEquals("S01", response.code)

        assertNull(response.data)
    }

    // @Test
    fun given_whenEmptyPhotoDelete_thenWorksFine() = runBlocking {
        // given
        val responseJson = """
            {
              "status": 200,
              "message": "등록된 프로필 이미지가 없습니다.",
              "code": "S16",
              "data": null
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )

        // when
        val response = sut.deleteUserImage()
        val recordedRequest = mockWebServer.takeRequest()

        // then
        assertEquals("/api/v1/members/profile", recordedRequest.path)

        // 응답 body 검증
        assertEquals(200, response.status)
        assertEquals("등록된 프로필 이미지가 없습니다.", response.message)
        assertEquals("S16", response.code)

        assertNull(response.data)
    }
}
