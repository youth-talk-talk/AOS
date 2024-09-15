package com.core.community.model

sealed interface CommunityUiEvent {
    data class PostScrap(val postId: Long, val scrap: Boolean) : CommunityUiEvent
}
