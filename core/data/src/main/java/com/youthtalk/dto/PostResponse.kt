package com.youthtalk.dto

import com.youthtalk.dto.post.PostResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    @SerialName("top5Posts") val popularPosts: List<PostResponse>,
    @SerialName("allPosts") val posts: List<PostResponse>
)

@Serializable
data class PostSearchResponse(
    @SerialName("total") val total: Int,
    @SerialName("posts") val posts: List<PostResponse>
)
