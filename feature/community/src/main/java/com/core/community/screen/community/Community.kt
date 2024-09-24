package com.core.community.screen.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.community.component.SearchBarComponent
import com.core.community.model.CommunityUiState
import com.youthtalk.model.Category
import com.youthtalk.util.clickableSingle

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Community(
    tabIndex: Int,
    tabNames: List<String>,
    lazyListState: LazyListState,
    uiState: CommunityUiState.Success,
    changeTab: (Int) -> Unit,
    changeReviewCheckBox: (Category?) -> Unit,
    onClickItem: (Long) -> Unit,
    writePost: (String) -> Unit,
    onClickSearch: (String) -> Unit,
    postPostScrap: (Long, Boolean) -> Unit,
    clearData: () -> Unit,
) {
    val categories = uiState.categories
    val reviewPosts = uiState.reviewPosts.collectAsLazyPagingItems()
    val popularReviewPosts = uiState.popularReviewPosts
    val popularPosts = uiState.popularPosts
    val posts = uiState.posts.collectAsLazyPagingItems()
    var isRefresh by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(refreshing = isRefresh, onRefresh = {
        isRefresh = true
        if (tabIndex == 0) {
            reviewPosts.refresh()
        } else {
            posts.refresh()
        }
        isRefresh = false
    })

    LifecycleResumeEffect(key1 = Unit) {
        if (tabIndex == 0) {
            reviewPosts.refresh()
        } else {
            posts.refresh()
        }
        onPauseOrDispose {
            clearData()
        }
    }

    LaunchedEffect(reviewPosts.loadState.refresh) {
        if (reviewPosts.loadState.refresh is LoadState.NotLoading) {
            lazyListState.scrollToItem(uiState.index, uiState.offset)
        }
    }

    LaunchedEffect(posts.loadState.refresh) {
        if (posts.loadState.refresh is LoadState.NotLoading) {
            lazyListState.scrollToItem(uiState.index, uiState.offset)
        }
    }

    Surface {
        Box(modifier = Modifier.pullRefresh(refreshState)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSecondaryContainer),
                contentPadding = PaddingValues(bottom = 12.dp),
                state = lazyListState,
            ) {
                item {
                    CommunityTab(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        tabIndex,
                        tabNames.toList(),
                    ) { newIndex ->
                        changeTab(newIndex)
                    }
                }

                item {
                    SearchBarComponent(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 17.dp)
                            .padding(top = 24.dp)
                            .clickableSingle {
                                val type = when (tabIndex) {
                                    0 -> "review"
                                    else -> "post"
                                }
                                onClickSearch(type)
                            },
                    )
                }

                when (tabIndex) {
                    0 -> reviewPost(
                        reviewCategories = categories,
                        popularReviewPosts = popularReviewPosts,
                        reviewPosts = reviewPosts,
                        map = uiState.postScrapMap,
                        onCheck = { category ->
                            changeReviewCheckBox(category)
                        },
                        onClickItem = { postId -> onClickItem(postId) },
                        postPostScrap = postPostScrap,
                    )

                    1 -> freeBoard(
                        popularPosts = popularPosts,
                        posts = posts,
                        map = uiState.postScrapMap,
                        onClickItem = { postId -> onClickItem(postId) },
                        postPostScrap = postPostScrap,
                    )
                }
            }

            WriteButton(
                onClick = {
                    val type = when (tabIndex) {
                        0 -> "review"
                        else -> "post"
                    }
                    writePost(type)
                },
            )

            PullRefreshIndicator(true, refreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}
