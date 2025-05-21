package com.core.mypage.model.scrappolicy

import com.core.base.model.UiEffect

sealed interface ScrapPolicyUiEffect : UiEffect {
    data class ClickPolicy(val policyId: Long) : ScrapPolicyUiEffect
}
