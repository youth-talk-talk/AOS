package com.core.mypage.model.scrappolicy

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
sealed class ScrapPolicyUiState {
    @Immutable
    data object Loading : ScrapPolicyUiState()

    @Immutable
    data class Success(
        val policies: Flow<PagingData<Policy>> = emptyFlow(),
        val deleteScrapMap: Map<String, Boolean> = mapOf(),
    ) : ScrapPolicyUiState()
}
