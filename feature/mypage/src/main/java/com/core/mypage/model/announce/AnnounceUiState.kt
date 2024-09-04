package com.core.mypage.model.announce

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class AnnounceUiState {

    @Immutable
    data object Loading : AnnounceUiState()
//
//    @Immutable
//    data class Success(val announces: )
}
