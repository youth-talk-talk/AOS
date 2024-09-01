package com.youthtalk.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val commentId: Long,
    val nickname: String,
    val content: String,
    val isLikedByMember: Boolean,
)
