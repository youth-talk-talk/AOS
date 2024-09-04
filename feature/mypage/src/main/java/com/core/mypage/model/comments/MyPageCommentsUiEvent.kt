package com.core.mypage.model.comments

sealed interface MyPageCommentsUiEvent {
    data class GetData(val isMine: Boolean) : MyPageCommentsUiEvent
}
