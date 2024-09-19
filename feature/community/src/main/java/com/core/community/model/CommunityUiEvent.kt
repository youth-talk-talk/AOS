package com.core.community.model

sealed interface CommunityUiEvent {
    data class PostScrap(val postId: Long, val scrap: Boolean) : CommunityUiEvent
    data class SaveScrollPosition(val index: Int, val offset: Int) : CommunityUiEvent
    data object ClearData : CommunityUiEvent
}
