package com.youthtalk.specpolicy.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.youthtalk.model.Category
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
sealed class SpecPolicyUiState {

    @Immutable
    data object Loading : SpecPolicyUiState()

    @Immutable
    data class Success(
        val category: Category,
        val polices: Flow<PagingData<Policy>> = emptyFlow(),
        val policyCount: Int = 0,
        val filterInfo: FilterInfo,
        val scrap: Map<String, Boolean> = mapOf(),
    ) : SpecPolicyUiState()
}
