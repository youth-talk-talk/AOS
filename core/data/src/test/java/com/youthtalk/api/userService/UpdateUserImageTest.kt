package com.youthtalk.api.userService

import com.youthtalk.api.ApiTestUtils
import com.youthtalk.api.ApiTestUtils.createRetrofit
import com.youthtalk.api.interceptor.TestAuthInterceptor
import com.youthtalk.data.UserService
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

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
}
