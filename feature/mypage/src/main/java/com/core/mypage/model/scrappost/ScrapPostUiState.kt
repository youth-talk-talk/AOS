package com.core.mypage.model.scrappost

import androidx.paging.PagingData
import com.core.base.model.UiState
import com.core.navigation.model.ScrapPostType
import com.youthtalk.model.post.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ScrapPostUiState(
    val isLoading: Boolean,
    val posts: Flow<PagingData<Post>>,
    val count: Int,
    val type: ScrapPostType
) : UiState {
    companion object {
        val initState = ScrapPostUiState(
            isLoading = true,
            posts = emptyFlow(),
            type = ScrapPostType.MY,
            count = 0
        )
    }
}
