package com.youthtalk.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiTestUtils {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val converterFactory = json.asConverterFactory("application/json".toMediaType())

    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}
