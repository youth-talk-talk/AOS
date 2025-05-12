package com.youthtalk.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    @SerialName("top5Posts") val popularPosts: List<PostDataResponse>,
    @SerialName("allPosts") val posts: List<PostDataResponse>
)

@Serializable
data class PostSearchResponse(
    @SerialName("total") val total: Int,
    @SerialName("posts") val posts: List<PostDataResponse>
)

@Serializable
data class PostDataResponse(
    val postId: Long,
    val title: String,
    val writerId: Long?,
    val scraps: Int,
    val scrap: Boolean,
    val comments: Int,
    val policyId: String?,
    val policyTitle: String?
)

@Serializable
data class ScrapPostDataResponse(
    val scrapId: Long? = null,
    val postId: Long,
    val title: String,
    val writerId: Long?,
    val scraps: Int,
    val scrap: Boolean,
    val comments: Int,
    val policyId: String?,
    val policyTitle: String?
)
