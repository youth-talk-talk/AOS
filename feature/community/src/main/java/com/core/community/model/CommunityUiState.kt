package com.core.community.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.youthtalk.model.Category
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
interface CommunityUiState {

    @Immutable
    object Loading : CommunityUiState

    @Immutable
    data class Success(
        val categories: ImmutableList<Category> = persistentListOf(),
    ) : CommunityUiState
}
