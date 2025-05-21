package com.core.mypage.model.scrappolicy

import androidx.paging.PagingData
import com.core.base.model.UiState
import com.youthtalk.model.policy.Policy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ScrapPolicyUiState(
    val isLoading: Boolean,
    val policies: Flow<PagingData<Policy>>
) : UiState {
    companion object {
        val initState = ScrapPolicyUiState(
            isLoading = true,
            policies = emptyFlow()
        )
    }
}
