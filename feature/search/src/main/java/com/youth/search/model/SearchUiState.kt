package com.youth.search.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed class SearchUiState {
    data object Loading : SearchUiState()

    data class Success(
        val recentList: ImmutableList<String> = persistentListOf(),
    ) : SearchUiState()
}
