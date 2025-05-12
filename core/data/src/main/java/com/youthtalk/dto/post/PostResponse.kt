package com.youthtalk.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val postId: Long,
    val title: String,
    val writerId: Long,
    val policyId: Long?,
    val policyTitle: String?,
    val comments: Int,
    val contentPreview: String,
    val scraps: Int,
    val scrap: Boolean,
    val createdAt: String
)
