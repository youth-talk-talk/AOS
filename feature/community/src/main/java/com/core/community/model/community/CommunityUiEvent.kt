package com.core.community.model.community

import com.core.base.model.UiEvent
import com.youthtalk.model.post.PostSubject

sealed interface CommunityUiEvent : UiEvent {
    data class PostScrap(val postId: Long, val scrap: Boolean, val type: PostSubject) :
        CommunityUiEvent
    data object GetData : CommunityUiEvent
    data object GetPopularData : CommunityUiEvent
}
