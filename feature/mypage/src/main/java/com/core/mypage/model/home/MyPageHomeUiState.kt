package com.core.mypage.model.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.youthtalk.model.Policy
import com.youthtalk.model.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed class MyPageHomeUiState {
    @Immutable
    data object Loading : MyPageHomeUiState()

    @Immutable
    data class Success(
        val deadlinePolicies: ImmutableList<Policy> = persistentListOf(),
        val user: User,
    ) : MyPageHomeUiState()
}
