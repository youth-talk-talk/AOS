package com.youthtalk.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

internal class TestAuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer token")
            .addHeader("Content-Type", "application/json")
            .build()

        return chain.proceed(newRequest)
    }
}
