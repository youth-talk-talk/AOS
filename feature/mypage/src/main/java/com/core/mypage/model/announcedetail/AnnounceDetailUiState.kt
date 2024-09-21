package com.core.mypage.model.announcedetail

import com.youthtalk.model.AnnounceDetail

sealed class AnnounceDetailUiState {
    data object Loading : AnnounceDetailUiState()
    data class Success(
        val announceDetail: AnnounceDetail,
    ) : AnnounceDetailUiState()
}
