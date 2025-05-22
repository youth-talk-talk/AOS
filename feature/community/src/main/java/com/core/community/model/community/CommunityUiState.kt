package com.core.community.model.community

import androidx.paging.PagingData
import com.core.base.model.UiState
import com.youthtalk.model.post.Post
import com.youthtalk.model.typeenum.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CommunityUiState(
    val category: Category,
    val reviews: Flow<PagingData<Post>>,
    val frees: Flow<PagingData<Post>>,
    val popularReviews: List<Post>,
    val popularFrees: List<Post>
) : UiState {
    companion object {
        val initState = CommunityUiState(
            category = Category.ALL,
            popularFrees = listOf(),
            popularReviews = listOf(),
            reviews = emptyFlow(),
            frees = emptyFlow()
        )
    }
}
