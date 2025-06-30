package com.youth.search.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.youth.search.component.RecentText
import com.youth.search.component.SearchBar
import com.youth.search.model.SearchState
import com.youth.search.model.policysearch.PolicySearchUiEvent
import com.youth.search.viewmodel.PolicySearchViewModel
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray80
import com.youthtalk.extentions.rememberLazyListState
import timber.log.Timber

@Composable
fun PolicySearchScreen(
    viewModel: PolicySearchViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onClickPolicyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val policies = uiState.policies.collectAsLazyPagingItems()
    var search by rememberSaveable {
        mutableStateOf(uiState.searchFilter.keyword ?: "")
    }

    BackHandler {
        when (uiState.state) {
            SearchState.NONE -> onBack()
            SearchState.SEARCH -> viewModel.setEvent(PolicySearchUiEvent.SetState(SearchState.NONE))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = gray10)
    ) {
        SearchBar(
            text = search,
            onClickBack = onBack,
            onTextChange = { search = it },
            onSearch = {
                viewModel.setEvent(PolicySearchUiEvent.Search(it))
            },
            onClear = {
                search = ""
            }
        )

        Crossfade(
            targetState = uiState.state
        ) {
            when (it) {
                SearchState.NONE -> {
                    Column {
                        SearchScreen(
                            recents = uiState.recently,
                            onDelete = { search ->
                                viewModel.setEvent(PolicySearchUiEvent.SetRecently(uiState.recently.filter { value -> value != search }))
                            },
                            onDeleteAll = {
                                viewModel.setEvent(PolicySearchUiEvent.SetRecently(listOf()))
                            },
                            onClickItem = { item ->
                                search = item
                                viewModel.setEvent(PolicySearchUiEvent.Search(item))
                            }
                        )
                    }
                }

                SearchState.SEARCH -> {
                    PolicySearchResultScreen(
                        isLoading = uiState.searchLoading,
                        count = uiState.count,
                        searchFilter = uiState.searchFilter,
                        sortType = uiState.sortType,
                        policies = policies,
                        lazyListState = policies.rememberLazyListState(),
                        applyFilter = { searchFilter, sortType ->
                            viewModel.setEvent(PolicySearchUiEvent.SetFilter(searchFilter, sortType))
                        },
                        onClickPolicyDetail = onClickPolicyDetail,
                        onClickPostScrap = { id, scrap -> viewModel.setEvent(PolicySearchUiEvent.PolicyScrap(id, scrap)) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    recents: List<String>,
    onDeleteAll: () -> Unit,
    onDelete: (String) -> Unit,
    onClickItem: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 20.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "최근 검색",
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDeleteAll()
                },
            text = "전체 삭제",
            style = MaterialTheme.typography.labelSmall.copy(
                color = gray80
            )
        )
    }
    Timber.e("recents ${recents.size}")
    if (recents.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            recents.forEach {
                RecentText(
                    text = it,
                    onClick = { onClickItem(it) },
                    onDelete = { onDelete(it) }
                )
            }
        }
    } else {
        EmptyScreen(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            emptyTitle = "최근 검색된 내역이 없습니다."
        )
    }
}
