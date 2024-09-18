package com.core.community.model

sealed interface CommunityDetailUiEffect {
    data class CommunityWrite(val id: Long, val type: String) : CommunityDetailUiEffect
}
