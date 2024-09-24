package com.youth.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.youth.search.model.SearchResultUiEvent
import com.youth.search.model.SearchResultUiState
import com.youth.search.viewmodel.SearchResultViewModel

@Composable
fun SearchResultScreen(
    viewModel: SearchResultViewModel = hiltViewModel(),
    search: String,
    type: String,
    onClickDetailPolicy: (String) -> Unit,
    onClickDetailPost: (Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isRefresh by rememberSaveable {
        mutableStateOf(false)
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        if (type == "home") {
            viewModel.uiEvent(SearchResultUiEvent.GetPolicies(search))
        } else {
            viewModel.uiEvent(SearchResultUiEvent.GetPost(type, search))
        }
    }

    when (uiState) {
        is SearchResultUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is SearchResultUiState.Success -> {
            val state = uiState as SearchResultUiState.Success
            val policies = state.policies.collectAsLazyPagingItems()
            val posts = state.posts.collectAsLazyPagingItems()

            LifecycleResumeEffect(viewModel) {
                if (isRefresh) {
                    posts.refresh()
                    policies.refresh()
                    isRefresh = false
                }

                onPauseOrDispose {
                    viewModel.clearData()
                    isRefresh = true
                }
            }
            when (type) {
                "home" -> SearchPolicies(
                    policies,
                    state.count,
                    state.policyScrapMap,
                    state.filterInfo,
                    onClickDetailPolicy = onClickDetailPolicy,
                    onClickScrap = { postId, scrap -> viewModel.uiEvent(SearchResultUiEvent.PostPolicyScrap(postId, scrap)) },
                    postFilterInfo = { filterInfo -> viewModel.uiEvent(SearchResultUiEvent.PostFilterInfo(filterInfo)) },
                    getFilterInfo = { viewModel.uiEvent(SearchResultUiEvent.GetFilterInfo) },
                    onClickFilterApply = { viewModel.uiEvent(SearchResultUiEvent.FilterApply(search)) },
                )
                else -> SearchPosts(
                    posts,
                    state.count,
                    type,
                    map = state.postScrapMap,
                    onClickDetailPost = onClickDetailPost,
                    onClickScrap = { postId, scrap -> viewModel.uiEvent(SearchResultUiEvent.PostPostScrap(postId, scrap)) },
                )
            }
        }
    }
}
