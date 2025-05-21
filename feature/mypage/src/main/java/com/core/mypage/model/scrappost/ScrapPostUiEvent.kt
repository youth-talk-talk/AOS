package com.core.mypage.model.scrappost

import com.core.base.model.UiEvent
import com.core.navigation.model.ScrapPostType

sealed interface ScrapPostUiEvent : UiEvent {
    data class InitData(val isScrap: Boolean, val type: ScrapPostType) : ScrapPostUiEvent
    data object RefreshCount : ScrapPostUiEvent
    data class PostScrapPost(val postId: Long, val scrap: Boolean) : ScrapPostUiEvent
}
