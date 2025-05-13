package com.youth.search.model.policysearch

import androidx.paging.PagingData
import com.core.base.model.UiState
import com.youth.search.model.SearchState
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.search.SearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PolicySearchUiState(
    val searchLoading: Boolean,
    val state: SearchState,
    val recently: List<String>,
    val searchFilter: SearchFilter,
    val policies: Flow<PagingData<Policy>>,
    val count: Int
) : UiState {
    companion object {
        val initState = PolicySearchUiState(
            searchLoading = true,
            state = SearchState.NONE,
            recently = listOf(),
            searchFilter = SearchFilter(),
            policies = emptyFlow(),
            count = 0
        )
    }
}
