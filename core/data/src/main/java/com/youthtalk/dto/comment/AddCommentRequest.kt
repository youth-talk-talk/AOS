package com.youthtalk.dto.comment

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class AddCommentRequest(
    val postId: Long,
    val content: String
) {
    fun toRequestBody() = Json.encodeToString(serializer(), this).toRequestBody()
}

@Serializable
data class ModifyCommentRequest(
    val commentId: Long,
    val content: String
) {
    fun toRequestBody() = Json.encodeToString(serializer(), this).toRequestBody()
}
