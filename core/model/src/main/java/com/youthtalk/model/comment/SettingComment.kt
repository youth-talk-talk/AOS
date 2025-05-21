package com.youthtalk.model.comment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class SettingCommentInfo(
    val commentCount: Int,
    val comments: List<SettingComment>
)

data class SettingComment(
    val commentId: Long,
    val writerId: Long?,
    val nickname: String?,
    val content: String,
    val articleId: Long,
    val articleType: ArticleType,
    val articleTitle: String,
    val isLikedByMember: Boolean,
    val likeCount: Int
)

@Serializable
enum class ArticleType {
    @SerialName("post")
    POST,

    @SerialName("review")
    REVIEW,

    @SerialName("policy")
    POLICY
}
