package com.youthtalk.dto.comment

import com.youthtalk.model.comment.ArticleType
import kotlinx.serialization.Serializable

@Serializable
data class SettingCommentInfoResponse(
    val commentCount: Int,
    val comments: List<SettingCommentResponse>
)

@Serializable
data class SettingCommentResponse(
    val commentId: Long,
    val writerId: Long? = null,
    val nickname: String? = null,
    val content: String,
    val articleId: Long,
    val articleType: ArticleType,
    val articleTitle: String,
    val isLikedByMember: Boolean,
    val likeCount: Int
)
