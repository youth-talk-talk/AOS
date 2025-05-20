package com.youthtalk.dto.comment

import kotlinx.serialization.Serializable

@Serializable
data class CommentInfoResponse(
    val commentCount: Int,
    val comments: List<CommentResponse>
)

@Serializable
data class CommentResponse(
    val commentId: Long,
    val writerId: Long,
    val nickname: String,
    val content: String,
    val isLikedByMember: Boolean,
    val profileImg: String? = null,
    val createdAt: String
)
