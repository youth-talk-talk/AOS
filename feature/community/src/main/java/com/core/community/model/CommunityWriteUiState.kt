package com.core.community.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.youthtalk.model.SearchPolicy
import com.youthtalk.model.WriteInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
sealed class CommunityWriteUiState {
    @Immutable
    data object Loading : CommunityWriteUiState()

    @Immutable
    data class Success(
        val title: String = "",
        val contents: ImmutableList<WriteInfo> = persistentListOf(),
        val searchPolicies: Flow<PagingData<SearchPolicy>> = emptyFlow(),
        val selectPolicy: SearchPolicy? = null,
        val contentsInfo: ContentInfo = ContentInfo(0, 0),
        val isLoading: Boolean = false,
    ) : CommunityWriteUiState()
}
