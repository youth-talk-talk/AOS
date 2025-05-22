package com.core.home.model.newpolicy

import com.core.base.model.UiEvent
import com.youthtalk.model.typeenum.SortType

sealed interface NewPolicyUiEvent : UiEvent {
    data class GetNewPolices(val sortType: SortType = SortType.RECENT) : NewPolicyUiEvent
    data class OnClickPolicy(val policyId: Long) : NewPolicyUiEvent
    data class OnClickPolicyScrap(val policyId: Long, val scrap: Boolean) : NewPolicyUiEvent
    data object Refresh : NewPolicyUiEvent
}
