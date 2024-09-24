package com.youthtalk.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class LoginRequest(
    val socialType: String,
    val socialId: String,
) {
    fun toRequestBody() = Json.encodeToString(this).toRequestBody()
}
