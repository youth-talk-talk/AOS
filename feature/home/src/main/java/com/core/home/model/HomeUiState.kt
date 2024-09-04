package com.core.home.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.youthtalk.model.Category
import com.youthtalk.model.Policy
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
sealed interface HomeUiState {

    @Immutable
    data object Loading : HomeUiState

    @Immutable
    data class Success(
        val categoryList: ImmutableList<Category> = persistentListOf(),
        val popularPolicies: ImmutableList<Policy> = persistentListOf(),
        val allPolicies: Flow<PagingData<Policy>> = emptyFlow(),
        val scrap: Map<String, Boolean> = mapOf(),
    ) : HomeUiState
}
