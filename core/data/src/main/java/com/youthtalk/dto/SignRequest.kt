package com.youthtalk.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class SignRequest(
    val socialType: String,
    val socialId: String,
    val nickname: String,
    val region: String,
) {
    fun toRequestBody() = Json.encodeToString(this).toRequestBody()
}
