package com.feature.policy.model.recentlyview

import com.core.base.model.UiEvent

sealed interface RecentlyViewUiEvent : UiEvent {
    data object InitData : RecentlyViewUiEvent
    data class PostScrapPolicy(val policyId: Long, val scrap: Boolean) : RecentlyViewUiEvent
    data object DeleteAll : RecentlyViewUiEvent
    data class OnClickPolicy(val policyId: Long) : RecentlyViewUiEvent
}
