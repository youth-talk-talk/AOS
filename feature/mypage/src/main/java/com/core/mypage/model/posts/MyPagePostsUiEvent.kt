package com.core.mypage.model.posts

sealed interface MyPagePostsUiEvent {
    data class GetData(val type: String) : MyPagePostsUiEvent
}
