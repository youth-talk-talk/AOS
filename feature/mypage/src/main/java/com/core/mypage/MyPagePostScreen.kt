package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.core.mypage.model.posts.MyPagePostsUiEvent
import com.core.mypage.model.posts.MyPagePostsUiState
import com.core.mypage.viewmodel.MyPagePostViewModel
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.component.PostCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.PostType
import com.youthtalk.model.ScrapPost

@Composable
fun MyPagePostScreen(type: String, viewModel: MyPagePostViewModel = hiltViewModel(), onBack: () -> Unit, postDetail: (Long) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is MyPagePostsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is MyPagePostsUiState.Success -> {
            val success = uiState as MyPagePostsUiState.Success
            val posts = success.posts.collectAsLazyPagingItems()

            MyPagePosts(
                type = type,
                posts = posts,
                onBack = onBack,
                postDetail = postDetail,
                onClickScrap = { id, scrap, postType ->
                    viewModel.uiEvent(MyPagePostsUiEvent.PostScrap(id, scrap, postType))
                },
            )
        }
    }
}

@Composable
private fun MyPagePosts(
    type: String,
    posts: LazyPagingItems<ScrapPost>,
    onBack: () -> Unit,
    postDetail: (Long) -> Unit,
    onClickScrap: (Long, Boolean, PostType) -> Unit,
) {
    val lazyState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        MiddleTitleTopBar(
            title = if (type == "me") "작성한 게시물" else "스크랩한 게시물",
            onBack = onBack,
        )
        HorizontalDivider()

        LazyColumn(
            state = lazyState,
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 12.dp),
        ) {
            if (posts.loadState.refresh is LoadState.NotLoading) {
                items(
                    count = posts.itemCount,
                    key = posts.itemKey { it.postId },
                ) { index ->
                    posts[index]?.let { post ->
                        PostCard(
                            modifier = Modifier
                                .animateItem()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    },
                                ) {
                                    postDetail(post.postId)
                                },
                            policyTitle = post.policyTitle,
                            title = post.title,
                            scraps = post.scraps,
                            comments = post.comments,
                            scrap = post.scrap,
                            onClickScrap = { onClickScrap(post.postId, it, post.policyId?.let { PostType.REVIEW } ?: PostType.POST) },
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScrapPostScreenPreview() {
    val viewModel: MyPagePostViewModel = hiltViewModel()
    YongProjectTheme {
        MyPagePostScreen(
            type = "me",
            viewModel = viewModel,
            onBack = {},
            postDetail = {},
        )
    }
}
