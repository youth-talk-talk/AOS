package com.youth.search.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.youth.search.component.SearchBar
import com.youth.search.model.SearchState
import com.youth.search.model.communitysearch.CommunityUiEvent
import com.youth.search.viewmodel.CommunitySearchViewModel
import com.youthtalk.designsystem.gray10

@Composable
fun CommunitySearchScreen(modifier: Modifier = Modifier, viewModel: CommunitySearchViewModel = hiltViewModel(), onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var search by rememberSaveable {
        mutableStateOf("")
    }

    BackHandler {
        when (state.searchState) {
            SearchState.NONE -> onBack()
            SearchState.SEARCH -> viewModel.setEvent(CommunityUiEvent.SetState(SearchState.NONE))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = gray10)
    ) {
        SearchBar(
            text = search,
            onClickBack = {},
            onTextChange = { search = it },
            onSearch = { keyword ->
                viewModel.setEvent(CommunityUiEvent.Search(keyword, state.communityType))
            },
            onClear = {
                search = ""
            }
        )

        Crossfade(
            targetState = state.searchState
        ) {
            when (it) {
                SearchState.NONE -> {
                    Column {
                        SearchScreen(
                            recents = state.recently,
                            onDelete = {
                                viewModel.setEvent(CommunityUiEvent.SetRecently(state.recently.filter { value -> value != search }))
                            },
                            onDeleteAll = {
                                viewModel.setEvent(CommunityUiEvent.SetRecently(listOf()))
                            },
                            onClickItem = { keyword ->
                                viewModel.setEvent(CommunityUiEvent.Search(keyword, state.communityType))
                            }
                        )
                    }
                }

                SearchState.SEARCH -> {
                    CommunitySearchResultScreen(
                        communityType = state.communityType,
                        posts = state.searchPost.collectAsLazyPagingItems(),
                        totalCount = state.totalCount
                    )
                }
            }
        }
    }
}
