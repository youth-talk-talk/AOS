package com.youth.search.model.policysearch

import com.core.base.model.UiEvent
import com.youth.search.model.SearchState
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.SortType

sealed interface PolicySearchUiEvent : UiEvent {
    data object InitData : PolicySearchUiEvent
    data class SetRecently(val recently: List<String>) : PolicySearchUiEvent
    data class SetState(val state: SearchState) : PolicySearchUiEvent
    data class Search(val search: String) : PolicySearchUiEvent
    data class SetFilter(val searchFilter: SearchFilter, val sortType: SortType) : PolicySearchUiEvent
    data class PolicyScrap(val policyId: Long, val scrap: Boolean) : PolicySearchUiEvent
}
