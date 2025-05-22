package com.youth.search.model.communitysearch

import com.core.base.model.UiEffect

sealed interface CommunityUiEffect : UiEffect {
    data class ClickPost(val postId: Long) : CommunityUiEffect
}
