package com.core.mypage.model.announce

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.youthtalk.model.Announce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
sealed class AnnounceUiState {

    @Immutable
    data object Loading : AnnounceUiState()

    @Immutable
    data class Success(
        val announces: Flow<PagingData<Announce>> = emptyFlow(),
    ) : AnnounceUiState()
}
