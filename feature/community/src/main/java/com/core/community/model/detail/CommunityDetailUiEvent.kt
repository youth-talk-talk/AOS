package com.core.community.model.detail

import com.core.base.model.UiEvent
import com.youthtalk.model.Comment

sealed interface CommunityDetailUiEvent : UiEvent {
    data class InitData(val postId: Long) : CommunityDetailUiEvent
    data class ChangeDetailType(val detailType: CommunityDetailType) : CommunityDetailUiEvent
    data class PostAddComment(val postId: Long, val message: String) : CommunityDetailUiEvent
    data class PatchModifyComment(val commentId: Long, val message: String) : CommunityDetailUiEvent
    data class DeletePost(val postId: Long) : CommunityDetailUiEvent
    data class PostPostScrap(val postId: Long, val scrap: Boolean) : CommunityDetailUiEvent
    data class PostDeleteComment(val comment: Comment) : CommunityDetailUiEvent
}
