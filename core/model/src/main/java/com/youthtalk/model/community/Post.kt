package com.youthtalk.model.community

import java.time.LocalDateTime

data class Post(
    val postId: Long,
    val title: String,
    val writerId: Long,
    val policyId: Long?,
    val policyTitle: String?,
    val comments: Int,
    val contentPreview: String,
    val scraps: Int,
    val scrap: Boolean,
    val createdAt: LocalDateTime
)
