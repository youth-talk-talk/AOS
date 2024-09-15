package com.youthtalk.dto.community

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class PatchCommentRequest(
    val commentId: Long,
    val content: String,
) {
    fun toRequestBody() = Json.encodeToString(serializer(), this).toRequestBody()
}
