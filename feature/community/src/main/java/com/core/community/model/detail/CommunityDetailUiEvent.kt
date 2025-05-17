package com.core.community.model.detail

import com.core.base.model.UiEvent

sealed interface CommunityDetailUiEvent : UiEvent {
    data class InitData(val postId: Long) : CommunityDetailUiEvent
    data class ChangeDetailType(val detailType: CommunityDetailType) : CommunityDetailUiEvent
    data class OnPostAddComment(val postId: Long, val message: String) : CommunityDetailUiEvent
}
