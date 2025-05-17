package com.youth.search.model.communitysearch

import androidx.paging.PagingData
import com.core.base.model.UiState
import com.youth.search.model.SearchState
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CommunityUiState(
    val recently: List<String>,
    val communityType: PostSubject,
    val searchState: SearchState,
    val searchPost: Flow<PagingData<Post>>,
    val totalCount: Int
) : UiState {
    companion object {
        val initState = CommunityUiState(
            recently = listOf(),
            searchState = SearchState.NONE,
            communityType = PostSubject.POST,
            searchPost = emptyFlow(),
            totalCount = 0
        )
    }
}
