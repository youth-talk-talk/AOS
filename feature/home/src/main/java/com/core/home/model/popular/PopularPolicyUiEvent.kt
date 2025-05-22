package com.core.home.model.popular

import com.core.base.model.UiEvent

sealed interface PopularPolicyUiEvent : UiEvent {
    data class OnClickPolicy(val policyId: Long) : PopularPolicyUiEvent
    data class OnClickPolicyScrap(val policyId: Long, val scrap: Boolean) : PopularPolicyUiEvent
    data object Refresh : PopularPolicyUiEvent
}
