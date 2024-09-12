package com.youth.search.model

import androidx.paging.PagingData
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import com.youthtalk.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

sealed class SearchResultUiState {
    data object Loading : SearchResultUiState()

    data class Success(
        val policies: Flow<PagingData<Policy>> = emptyFlow(),
        val posts: Flow<PagingData<Post>> = emptyFlow(),
        val filterInfo: FilterInfo,
        val policyScrapMap: Map<String, Boolean> = mapOf(),
        val postScrapMap: Map<Long, Boolean> = mapOf(),
        val count: Int = 0,
    ) : SearchResultUiState()
}
