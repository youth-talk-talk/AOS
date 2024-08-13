package com.core.community.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.youthtalk.model.Category
import com.youthtalk.model.ReviewPost
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
interface CommunityUiState {

    @Immutable
    object Loading : CommunityUiState

    @Immutable
    data class Success(
        val categories: ImmutableList<Category> = persistentListOf(),
        val popularReviewPosts: ImmutableList<ReviewPost> = persistentListOf(),
        val reviewPosts: Flow<PagingData<ReviewPost>> = emptyFlow(),
    ) : CommunityUiState
}
