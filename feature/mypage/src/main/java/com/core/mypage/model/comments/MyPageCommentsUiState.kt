package com.core.mypage.model.comments

import com.youthtalk.model.Comment
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed class MyPageCommentsUiState {
    data object Loading : MyPageCommentsUiState()

    data class Success(
        val comments: ImmutableList<Comment> = persistentListOf(),
    ) : MyPageCommentsUiState()
}
