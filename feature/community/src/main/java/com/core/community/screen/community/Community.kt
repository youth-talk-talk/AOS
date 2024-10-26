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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.paging.compose.LazyPagingItems
import com.core.community.component.SearchBarComponent
import com.youth.app.feature.community.R
import com.youthtalk.model.Category
import com.youthtalk.model.Post
import com.youthtalk.model.PostType
import com.youthtalk.model.ReviewPost
import com.youthtalk.util.clickableSingle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Community(
    reviewPosts: LazyPagingItems<ReviewPost>,
    posts: LazyPagingItems<Post>,
    categories: ImmutableList<Category>,
    popularReviewPosts: ImmutableList<ReviewPost>,
    popularPosts: ImmutableList<Post>,
    changeReviewCheckBox: (Category?) -> Unit,
    onClickItem: (Long) -> Unit,
    writePost: (String) -> Unit,
    onClickSearch: (String) -> Unit,
    postPostScrap: (Long, Boolean, PostType) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tabNames = stringArrayResource(id = R.array.tabs)
    var tabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val isRefresh by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(
        refreshing = isRefresh,
        onRefresh = {
            if (tabIndex == 0) {
                reviewPosts.refresh()
            } else {
                posts.refresh()
            }
        },
    )
    val lazyListState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }
    var scrollInfo by rememberSaveable {
        mutableStateOf(Pair(0, 0))
    }

    LifecycleResumeEffect(Unit) {
        scope.launch {
            lazyListState.scrollToItem(scrollInfo.first, scrollInfo.second)
        }
        onPauseOrDispose {
            scrollInfo = Pair(lazyListState.firstVisibleItemIndex, lazyListState.firstVisibleItemScrollOffset)
        }
    }

    Box(
        modifier = Modifier
            .pullRefresh(refreshState),
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSecondaryContainer),
            contentPadding = PaddingValues(bottom = 12.dp),
        ) {
            item {
                CommunityTab(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    tabIndex,
                    tabNames.toList(),
                ) { newIndex ->
                    tabIndex = newIndex
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
                    onCheck = { category ->
                        changeReviewCheckBox(category)
                    },
                    onClickItem = { postId -> onClickItem(postId) },
                    postPostScrap = postPostScrap,
                )

                1 -> freeBoard(
                    popularPosts = popularPosts,
                    posts = posts,
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

        PullRefreshIndicator(isRefresh, refreshState, Modifier.align(Alignment.TopCenter))
    }
}
