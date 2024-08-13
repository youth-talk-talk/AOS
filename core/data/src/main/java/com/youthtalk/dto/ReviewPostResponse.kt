package com.youthtalk.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewPostResponse(
    @SerialName("top5_posts") val popularReviewPosts: List<ReviewPostDataResponse>,
    @SerialName("other_posts") val reviewPosts: List<ReviewPostDataResponse>,
)

@Serializable
data class ReviewPostDataResponse(
    val postId: Long,
    val title: String,
    val content: String,
    val writerId: Long,
    val scraps: Int,
    val scrap: Boolean,
    val comments: Int,
    val policyId: String,
    val policyTitle: String,
)
