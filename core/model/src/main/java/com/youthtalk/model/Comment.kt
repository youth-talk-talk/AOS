package com.youthtalk.model

import java.time.LocalDateTime

data class CommentInfo(
    val commentCount: Int,
    val comments: List<Comment>
)

data class Comment(
    val commentId: Long,
    val writerId: Long,
    val nickname: String,
    val content: String,
    val isLikedByMember: Boolean,
    val profileImg: String? = null,
    val createdAt: LocalDateTime
)
