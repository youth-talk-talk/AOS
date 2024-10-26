package com.core.community.model

import com.youthtalk.model.PostType

sealed interface CommunityUiEvent {
    data class PostScrap(val postId: Long, val scrap: Boolean, val type: PostType) : CommunityUiEvent
    data object GetData : CommunityUiEvent
}
