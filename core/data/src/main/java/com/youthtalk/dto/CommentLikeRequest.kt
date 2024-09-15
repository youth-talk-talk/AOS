package com.youthtalk.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class CommentLikeRequest(
    val commentId: Long,
    val isSetLiked: Boolean,
) {
    fun toRequestBody() = Json.encodeToString(serializer(), this).toRequestBody()
}
