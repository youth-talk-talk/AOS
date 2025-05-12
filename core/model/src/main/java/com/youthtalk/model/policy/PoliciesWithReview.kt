package com.youthtalk.model.policy

import java.time.LocalDateTime

data class PoliciesWithReview(
    val policyId: Long,
    val title: String,
    val departmentImgUrl: String?,
    val reviews: List<Review>
)

data class Review(
    val postId: Long,
    val title: String,
    val contentPreview: String,
    val commentCount: Int,
    val scrapCount: Int,
    val createdAt: LocalDateTime
)
