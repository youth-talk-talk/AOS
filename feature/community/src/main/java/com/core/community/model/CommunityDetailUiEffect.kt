package com.core.community.model

sealed interface CommunityDetailUiEffect {
    data class CommunityWrite(val id: Long, val type: String) : CommunityDetailUiEffect
    data class PostDelete(val isDelete: Boolean) : CommunityDetailUiEffect
    data class NotFoundPost(val isDelete: Boolean, val message: String?) : CommunityDetailUiEffect
}
