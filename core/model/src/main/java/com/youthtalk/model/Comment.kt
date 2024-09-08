package com.youthtalk.model

data class Comment(
    val commentId: Long,
    val nickname: String,
    val content: String,
    val isLikedByMember: Boolean,
    val policyId: String? = null,
    val postId: Long? = null,
)
