package com.youthtalk.api

import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiTestUtils {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val converterFactory = json.asConverterFactory("application/json".toMediaType())

    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun createRetrofit(baseUrl: HttpUrl, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(converterFactory)
        .build()
}
