package com.core.community.screen.community

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.core.community.model.CommunityUiEvent
import com.core.community.model.CommunityUiState
import com.core.community.viewmodel.CommunityViewModel
import com.youth.app.feature.community.R
import com.youthtalk.component.PolicyCheckBox
import com.youthtalk.component.PostCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Post
import com.youthtalk.model.PostType
import com.youthtalk.model.ReviewPost
import com.youthtalk.util.clickableSingle
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CommunityScreen(
    isRemove: Boolean = false,
    viewModel: CommunityViewModel = hiltViewModel(),
    onClickItem: (Long) -> Unit,
    writePost: (String) -> Unit,
    onClickSearch: (String) -> Unit,
) {
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
        val reviewPost = state.reviewPosts.collectAsLazyPagingItems()
        val posts = state.posts.collectAsLazyPagingItems()

        LaunchedEffect(key1 = isRemove) {
            Log.d("YOON-CHAN", "getData")
            if (isRemove) viewModel.uiEvent(CommunityUiEvent.GetData)
        }

        Community(
            reviewPosts = reviewPost,
            posts = posts,
            categories = state.categories,
            popularReviewPosts = state.popularReviewPosts,
            popularPosts = state.popularPosts,
            changeReviewCheckBox = viewModel::setCategories,
            onClickItem = onClickItem,
            writePost = writePost,
            onClickSearch = onClickSearch,
            postPostScrap = { postId, scrap, type -> viewModel.uiEvent(CommunityUiEvent.PostScrap(postId, scrap, type)) },
        )
    }
}

fun LazyListScope.reviewPost(
    reviewCategories: ImmutableList<Category>,
    popularReviewPosts: ImmutableList<ReviewPost>,
    reviewPosts: LazyPagingItems<ReviewPost>,
    onCheck: (Category?) -> Unit,
    onClickItem: (Long) -> Unit,
    postPostScrap: (Long, Boolean, PostType) -> Unit,
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
            PopularPosts(popularReviewPosts, onClickItem, postPostScrap = postPostScrap)
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

    if (reviewPosts.loadState.refresh is LoadState.NotLoading) {
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
                reviewPosts[index]?.let { reviewPost ->
                    PostCard(
                        modifier = Modifier
                            .clickableSingle { onClickItem(reviewPost.postId) },
                        title = reviewPost.title,
                        comments = reviewPost.comments,
                        scrap = reviewPost.scrap,
                        scraps = reviewPost.scraps,
                        policyTitle = reviewPost.policyTitle,
                        onClickScrap = { postPostScrap(reviewPost.postId, it, PostType.REVIEW) },
                    )
                }
            }
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
    onClickItem: (Long) -> Unit,
    postPostScrap: (Long, Boolean, PostType) -> Unit,
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
                    PostCard(
                        modifier = Modifier
                            .aspectRatio(2.5f)
                            .clickableSingle { onClickItem(popularPost.postId) },
                        policyTitle = popularPost.policyTitle,
                        title = popularPost.title,
                        scraps = popularPost.scraps,
                        comments = popularPost.comments,
                        scrap = popularPost.scrap,
                        isSingleLine = true,
                        onClickScrap = { postPostScrap(popularPost.postId, it, PostType.POST) },
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

    if (posts.loadState.refresh is LoadState.NotLoading) {
        items(
            count = posts.itemCount,
            key = posts.itemKey { it.postId },
            contentType = posts.itemContentType { it.postId },
        ) { index ->
            posts[index]?.let { post ->
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
                            .clickableSingle { onClickItem(post.postId) },
                        policyTitle = post.policyTitle,
                        title = post.title,
                        scraps = post.scraps,
                        comments = post.comments,
                        scrap = post.scrap,
                        onClickScrap = { postPostScrap(post.postId, it, PostType.POST) },
                    )
                }
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
