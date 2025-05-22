package com.feature.policy.model.recentlyview

import com.core.base.model.UiEffect

sealed interface RecentlyViewUiEffect : UiEffect {
    data class OnPolicyDetail(val policyId: Long) : RecentlyViewUiEffect
}
