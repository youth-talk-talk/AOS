package com.core.mypage.model.comment

import com.core.base.model.UiEvent
import com.core.navigation.model.CommentType
import com.youthtalk.model.comment.ArticleType
import com.youthtalk.model.comment.SettingComment

sealed interface CommentUiEvent : UiEvent {
    data class InitData(val type: CommentType) : CommentUiEvent
    data class OnClickModify(val comment: SettingComment) : CommentUiEvent
    data class OnDeleteComment(val comment: SettingComment) : CommentUiEvent
    data class OnPatchComment(val commentId: Long, val content: String) : CommentUiEvent
    data class PostCommentLike(val commentId: Long, val isLike: Boolean) : CommentUiEvent
    data class OnClickCard(val type: ArticleType, val id: Long) : CommentUiEvent
    data object OnBackMain : CommentUiEvent
    data object RefreshData : CommentUiEvent
}
