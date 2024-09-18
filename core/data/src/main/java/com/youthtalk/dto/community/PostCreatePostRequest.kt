package com.youthtalk.dto.community

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class PostCreatePostRequest(
    val title: String,
    val postType: String,
    val policyId: String?,
    val contentList: List<PostContentRequest>,
) {
    fun toRequestBody() = Json.encodeToString(serializer(), this).toRequestBody()
}

@Serializable
data class PostContentRequest(
    val content: String,
    val type: String,
)
