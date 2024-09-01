package com.youthtalk.dto.specpolicy

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class CommentRequest(
    val policyId: String,
    val content: String,
) {
    fun toRequestBody() = Json.encodeToString(serializer(), this).toRequestBody()
}
