package com.core.home.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.youthtalk.model.Category
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface HomeUiState {

    @Immutable
    data object Loading : HomeUiState

    @Immutable
    data class Success(
        val categoryList: ImmutableList<Category> = persistentListOf(),
    ) : HomeUiState
}
