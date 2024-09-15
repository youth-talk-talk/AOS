package com.core.community.model

import com.youthtalk.model.Comment
import com.youthtalk.model.PostDetail
import com.youthtalk.model.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed class CommunityDetailUiState {
    data object Loading : CommunityDetailUiState()

    data class Success(
        val post: PostDetail,
        val user: User,
        val comments: ImmutableList<Comment> = persistentListOf(),
    ) : CommunityDetailUiState()
}
