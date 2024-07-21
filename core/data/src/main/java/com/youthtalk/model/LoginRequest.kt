package com.youthtalk.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class LoginRequest(
    val username: String,
) {
    fun toRequestBody() = Json.encodeToString(this).toRequestBody()
}
