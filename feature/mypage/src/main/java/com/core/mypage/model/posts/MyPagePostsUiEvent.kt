package com.core.mypage.model.posts

import com.youthtalk.model.PostType

sealed interface MyPagePostsUiEvent {
    data class PostScrap(val id: Long, val scrap: Boolean, val type: PostType) : MyPagePostsUiEvent
}
