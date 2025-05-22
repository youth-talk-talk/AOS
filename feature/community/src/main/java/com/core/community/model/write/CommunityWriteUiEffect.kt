package com.core.community.model.write

import com.core.base.model.UiEffect
import com.youthtalk.model.Image

sealed interface CommunityWriteUiEffect : UiEffect {
    data class GoPictureScreen(val data: List<Image>) : CommunityWriteUiEffect
    data object OnBack : CommunityWriteUiEffect
    data class ScrollIndex(val index: Int) : CommunityWriteUiEffect
    data object CreatePost : CommunityWriteUiEffect
    data class Modify(val postId: Long) : CommunityWriteUiEffect
}
