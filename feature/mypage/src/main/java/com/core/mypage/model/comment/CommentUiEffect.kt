package com.core.mypage.model.comment

import com.core.base.model.UiEffect

sealed interface CommentUiEffect : UiEffect {
    data class ModifyInfo(val commentId: Long, val content: String) : CommentUiEffect
    data class ShowSnackBar(val message: String) : CommentUiEffect
    data class DetailPost(val postId: Long) : CommentUiEffect
    data class DetailPolicy(val policyId: Long) : CommentUiEffect
}
