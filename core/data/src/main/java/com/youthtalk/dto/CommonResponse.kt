package com.youthtalk.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.ResponseBody.Companion.toResponseBody

@Serializable
data class CommonResponse<T>(
    val status: Int,
    val message: String,
    val code: String,
    val data: T?,
)

inline fun <reified T> toResponseBody(request: CommonResponse<T>) = Json.encodeToString(serializer<CommonResponse<T>>(), request).toResponseBody()
