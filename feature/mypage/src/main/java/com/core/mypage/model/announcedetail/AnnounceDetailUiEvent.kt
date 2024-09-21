package com.core.mypage.model.announcedetail

sealed interface AnnounceDetailUiEvent {
    data class GetAnnounceDetail(val id: Long) : AnnounceDetailUiEvent
}
