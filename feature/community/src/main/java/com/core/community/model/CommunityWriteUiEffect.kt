package com.core.community.model

sealed interface CommunityWriteUiEffect {
    data class GoDetail(val id: Long) : CommunityWriteUiEffect
}
