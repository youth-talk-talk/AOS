package com.youthtalk.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class PostUserRequest(
    val nickname: String,
    val region: String,
) {
    fun toRequestBody(): RequestBody = Json.encodeToString(serializer(), this).toRequestBody()
}
