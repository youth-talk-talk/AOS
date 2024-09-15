package com.youthtalk.dto.community

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class PostAddCommentRequest(
    val postId: Long,
    val content: String,
) {
    fun toRequest() = Json.encodeToString(serializer(), this).toRequestBody()
}
