package com.core.mypage.model.posts

sealed interface MyPagePostsUiEvent {
    data class GetData(val type: String) : MyPagePostsUiEvent
    data class PostScrap(val id: Long, val scrap: Boolean) : MyPagePostsUiEvent
}
