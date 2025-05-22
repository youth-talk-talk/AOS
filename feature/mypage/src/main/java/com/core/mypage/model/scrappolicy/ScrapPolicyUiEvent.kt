package com.core.mypage.model.scrappolicy

import com.core.base.model.UiEvent

sealed interface ScrapPolicyUiEvent : UiEvent {
    data object InitData : ScrapPolicyUiEvent
    data class PostScrap(val policyId: Long, val scrap: Boolean) : ScrapPolicyUiEvent
    data class OnClickPolicy(val policyId: Long) : ScrapPolicyUiEvent
}
