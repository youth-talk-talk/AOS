package com.core.community.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.core.community.component.SearchBarComponent
import com.core.community.model.CommunityUiEvent
import com.core.community.model.CommunityUiState
import com.core.community.viewmodel.CommunityViewModel
import com.youth.app.feature.community.R
import com.youthtalk.component.PolicyCheckBox
import com.youthtalk.component.PostCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Post
import com.youthtalk.util.clickableSingle
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel = hiltViewModel(),
    onClickItem: (Long) -> Unit,
    writePost: (String) -> Unit,
    onClickSearch: (String) -> Unit,
) {
    val tabNames = stringArrayResource(id = R.array.tabs)
    var tabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState !is CommunityUiState.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        val state = uiState as CommunityUiState.Success
        val lazyListState = rememberLazyListState(
            initialFirstVisibleItemIndex = state.index,
            initialFirstVisibleItemScrollOffset = state.offset,
        )

        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset }
                .collect { (index, offset) ->
                    viewModel.uiEvent(CommunityUiEvent.SaveScrollPosition(index, offset))
                }
        }
        CommunitySuccessScreen(
            tabIndex,
            tabNames.toList(),
            lazyListState = lazyListState,
            state,
            changeTab = { index -> tabIndex = index },
            changeReviewCheckBox = viewModel::setCategories,
            onClickItem = onClickItem,
            writePost = writePost,
            onClickSearch = onClickSearch,
            postPostScrap = { postId, scrap -> viewModel.uiEvent(CommunityUiEvent.PostScrap(postId, scrap)) },
            clearData = { viewModel.uiEvent(CommunityUiEvent.ClearData) },
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CommunitySuccessScreen(
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

@Composable
private fun BoxScope.WriteButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier.Companion
            .align(Alignment.BottomCenter)
            .padding(bottom = 12.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50.dp),
            )
            .clickableSingle {
                onClick()
            }
            .padding(
                horizontal = 17.dp,
                vertical = 13.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.write_icon),
            contentDescription = "글쓰기",
            tint = Color.Black,
        )
        Text(
            text = "글쓰기",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
private fun CommunityTab(modifier: Modifier = Modifier, tabIndex: Int, tabNames: List<String>, onTabClick: (Int) -> Unit) {
    TabRow(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(top = 17.dp),
        selectedTabIndex = tabIndex,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabIndex])
                    .padding(vertical = 1.dp)
                    .offset(y = 0.5.dp)
                    .height(3.dp)
                    .background(Color.Black, shape = RoundedCornerShape(3.dp)),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        divider = {
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.onTertiary, shape = RoundedCornerShape(2.dp)),
                color = Color.Transparent,
            )
        },
    ) {
        tabNames.forEachIndexed { index, s ->
            Tab(
                selectedContentColor = MaterialTheme.colorScheme.background,
                selected = index == tabIndex,
                onClick = { onTabClick(index) },
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = s,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = if (index == tabIndex) Color.Black else MaterialTheme.colorScheme.onTertiary,
                    ),
                )
            }
        }
    }
}

private fun LazyListScope.reviewPost(
    reviewCategories: ImmutableList<Category>,
    popularReviewPosts: ImmutableList<Post>,
    reviewPosts: LazyPagingItems<Post>,
    map: Map<Long, Boolean>,
    onCheck: (Category?) -> Unit,
    onClickItem: (Long) -> Unit,
    postPostScrap: (Long, Boolean) -> Unit,
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 12.dp, start = 23.dp, end = 24.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CheckBoxScreen(reviewCategories, onCheck)
        }
    }

    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                )
                .padding(
                    top = 15.dp,
                    start = 17.dp,
                    end = 17.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PopularPosts(popularReviewPosts, map, onClickItem, postPostScrap = postPostScrap)
        }
    }

    item {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                .padding(
                    top = 12.dp,
                    start = 17.dp,
                    end = 17.dp,
                ),
            text = stringResource(id = R.string.recent_post),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onPrimary,
            ),
        )
    }

    items(
        count = reviewPosts.itemCount,
        key = reviewPosts.itemKey { it.postId },
        contentType = reviewPosts.itemContentType { it.postId },
    ) { index ->
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                .padding(horizontal = 17.dp)
                .padding(top = 12.dp),

        ) {
            reviewPosts[index]?.let { post ->
                val scrapPost = post.copy(
                    scrap = map.getOrDefault(post.postId, post.scrap),
                    scraps = if (post.scrap == map.getOrDefault(post.postId, post.scrap)) {
                        post.scraps
                    } else {
                        if (map.getOrDefault(post.postId, post.scrap)) post.scraps + 1 else post.scraps - 1
                    },
                )
                PostCard(
                    modifier = Modifier
                        .clickableSingle { onClickItem(scrapPost.postId) },
                    title = scrapPost.title,
                    comments = scrapPost.comments,
                    scrap = scrapPost.scrap,
                    scraps = scrapPost.scraps,
                    policyTitle = scrapPost.policyTitle,
                    onClickScrap = { postPostScrap(scrapPost.postId, it) },
                )
            }
        }
    }
}

@Composable
private fun PopularPosts(
    popularReviewPosts: ImmutableList<Post>,
    map: Map<Long, Boolean>,
    onClickItem: (Long) -> Unit,
    postPostScrap: (Long, Boolean) -> Unit,
) {
    Text(
        text = stringResource(id = R.string.popular_post),
        style = MaterialTheme.typography.headlineSmall.copy(
            color = MaterialTheme.colorScheme.onPrimary,
        ),
    )

    LazyRow(
        modifier = Modifier
            .aspectRatio(2.8f),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = popularReviewPosts.size,
        ) { index ->
            val reviewPost = popularReviewPosts[index]
            val post = reviewPost.copy(
                scrap = map.getOrDefault(reviewPost.postId, reviewPost.scrap),
                scraps = if (reviewPost.scrap == map.getOrDefault(reviewPost.postId, reviewPost.scrap)) {
                    reviewPost.scraps
                } else {
                    if (map.getOrDefault(reviewPost.postId, reviewPost.scrap)) reviewPost.scraps + 1 else reviewPost.scraps - 1
                },
            )
            PostCard(
                modifier = Modifier
                    .aspectRatio(2.5f)
                    .clickableSingle {
                        onClickItem(post.postId)
                    },
                policyTitle = post.policyTitle,
                title = post.title,
                comments = post.comments,
                scraps = post.scraps,
                scrap = post.scrap,
                isSingleLine = true,
                onClickScrap = { postPostScrap(post.postId, it) },
            )
        }
    }
}

@Composable
private fun CheckBoxScreen(reviewCategories: ImmutableList<Category>, onCheck: (Category?) -> Unit) {
    stringArrayResource(id = R.array.policies).forEach { category ->
        PolicyCheckBox(
            spaceBy = Arrangement.spacedBy(7.dp),
            isCheck = reviewCategories.any { checkCategory ->
                checkCategory.categoryName == category
            },
            title = category,
            textStyle = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary,
            ),
            onCheckChange = {
                onCheck(Category.entries.find { it.categoryName == category })
            },
        )
    }
}

fun LazyListScope.freeBoard(
    popularPosts: List<Post>,
    posts: LazyPagingItems<Post>,
    map: Map<Long, Boolean>,
    onClickItem: (Long) -> Unit,
    postPostScrap: (Long, Boolean) -> Unit,
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 24.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                )
                .padding(
                    top = 15.dp,
                    start = 17.dp,
                    end = 17.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.popular_post),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                ),
            )

            LazyRow(
                modifier = Modifier
                    .aspectRatio(2.8f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    count = popularPosts.size,
                ) { index ->
                    val popularPost = popularPosts[index]
                    val post = if (map.containsKey(popularPost.postId)) {
                        popularPost.copy(
                            scrap = map.getOrDefault(popularPost.postId, popularPost.scrap),
                            scraps = if (popularPost.scrap == map.getOrDefault(popularPost.postId, popularPost.scrap)) {
                                popularPost.scraps
                            } else {
                                if (map.getOrDefault(popularPost.postId, popularPost.scrap)) popularPost.scraps + 1 else popularPost.scraps - 1
                            },
                        )
                    } else {
                        popularPost
                    }
                    PostCard(
                        modifier = Modifier
                            .aspectRatio(2.5f)
                            .clickableSingle { onClickItem(post.postId) },
                        policyTitle = post.policyTitle,
                        title = post.title,
                        scraps = post.scraps,
                        comments = post.comments,
                        scrap = post.scrap,
                        isSingleLine = true,
                        onClickScrap = { postPostScrap(post.postId, it) },
                    )
                }
            }
        }
    }

    item {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                .padding(
                    top = 12.dp,
                    start = 17.dp,
                    end = 17.dp,
                ),
            text = stringResource(id = R.string.recent_post),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onPrimary,
            ),
        )
    }

    items(
        count = posts.itemCount,
        key = posts.itemKey { it.postId },
        contentType = posts.itemContentType { it.postId },
    ) { index ->
        posts[index]?.let { post ->
            val scrapPost = if (map.containsKey(post.postId)) {
                post.copy(
                    scrap = map.getOrDefault(post.postId, post.scrap),
                    scraps = if (post.scrap == map.getOrDefault(post.postId, post.scrap)) {
                        post.scraps
                    } else {
                        if (map.getOrDefault(post.postId, post.scrap)) post.scraps + 1 else post.scraps - 1
                    },
                )
            } else {
                post
            }
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    .padding(horizontal = 17.dp)
                    .padding(top = 12.dp),

            ) {
                PostCard(
                    modifier = Modifier
                        .clickableSingle { onClickItem(scrapPost.postId) },
                    policyTitle = scrapPost.policyTitle,
                    title = scrapPost.title,
                    scraps = scrapPost.scraps,
                    comments = scrapPost.comments,
                    scrap = scrapPost.scrap,
                    onClickScrap = { postPostScrap(scrapPost.postId, it) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommunityScreenPreview() {
    YongProjectTheme {
        CommunityScreen(
            onClickItem = {},
            writePost = {},
            onClickSearch = {},
        )
    }
}
