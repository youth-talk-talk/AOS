package com.youthtalk.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class SignRequest(
    val username: String,
    val nickname: String,
    val region: String,
) {
    fun toRequestBody() = Json.encodeToString(this).toRequestBody()
}
