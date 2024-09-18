package com.core.mypage.model.posts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.youthtalk.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
sealed class MyPagePostsUiState {
    @Immutable
    data object Loading : MyPagePostsUiState()

    @Immutable
    data class Success(
        val posts: Flow<PagingData<Post>> = emptyFlow(),
        val scrapMap: Map<Long, Boolean> = mapOf(),
    ) : MyPagePostsUiState()
}
