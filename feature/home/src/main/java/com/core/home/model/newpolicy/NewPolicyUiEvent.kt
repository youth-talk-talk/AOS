package com.core.home.model.newpolicy

import com.core.base.model.UiEvent
import com.youthtalk.model.enum.SortType

sealed interface NewPolicyUiEvent : UiEvent {
    data class GetNewPolices(val sortType: SortType = SortType.RECENT) : NewPolicyUiEvent
}
