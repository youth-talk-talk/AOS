package com.youthtalk.dto.policy

import kotlinx.serialization.Serializable

@Serializable
data class PoliciesWithReviewResponse(
    val policyId: Long,
    val title: String,
    val departmentImgUrl: String?,
    val reviews: List<ReviewResponse>
)

@Serializable
data class ReviewResponse(
    val postId: Long,
    val title: String,
    val contentPreview: String,
    val commentCount: Int,
    val scrapCount: Int,
    val createdAt: String,
    val scrap: Boolean
)
