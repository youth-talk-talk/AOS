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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.core.mypage.model.posts.MyPagePostsUiEvent
import com.core.mypage.model.posts.MyPagePostsUiState
import com.core.mypage.viewmodel.MyPagePostViewModel
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.component.PostCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Post

@Composable
fun MyPagePostScreen(type: String, viewModel: MyPagePostViewModel = hiltViewModel(), onBack: () -> Unit, postDetail: (Long) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.uiEvent(MyPagePostsUiEvent.GetData(type = type))
    }

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
            var isOnPause by rememberSaveable { mutableStateOf(false) }

            LifecycleResumeEffect(key1 = Unit) {
                if (isOnPause) {
                    posts.refresh()
                    isOnPause = false
                }
                onPauseOrDispose {
                    isOnPause = true
                }
            }

            MyPagePosts(
                type = type,
                posts = posts,
                scrapMap = success.scrapMap,
                onBack = onBack,
                postDetail = postDetail,
                onClickScrap = { id, scrap -> viewModel.uiEvent(MyPagePostsUiEvent.PostScrap(id, scrap)) },
            )
        }
    }
}

@Composable
private fun MyPagePosts(
    type: String,
    posts: LazyPagingItems<Post>,
    scrapMap: Map<Long, Boolean>,
    onBack: () -> Unit,
    postDetail: (Long) -> Unit,
    onClickScrap: (Long, Boolean) -> Unit,
) {
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
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 12.dp),
        ) {
            items(
                count = posts.itemCount,
                key = posts.itemKey { it.postId },
            ) { index ->
                posts[index]?.let { post ->
                    val scrapPost = if (scrapMap.containsKey(post.postId)) {
                        post.copy(
                            scrap = scrapMap.getOrDefault(post.postId, post.scrap),
                            scraps = if (post.scrap) post.scraps - 1 else post.scraps + 1,
                        )
                    } else {
                        post
                    }

                    if (!(type == "scrap" && scrapMap.containsKey(post.postId))) {
                        PostCard(
                            modifier = Modifier
                                .animateItem()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    },
                                ) {
                                    postDetail(scrapPost.postId)
                                },
                            policyTitle = scrapPost.policyTitle,
                            title = scrapPost.title,
                            scraps = scrapPost.scraps,
                            comments = scrapPost.comments,
                            scrap = scrapPost.scrap,
                            onClickScrap = { onClickScrap(scrapPost.postId, it) },
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

fun getPost(): List<Post> {
    return listOf(
        Post(
            postId = 1,
            title = "게시글 타이틀1",
            content = "게시글 내용",
            writerId = 12,
            scraps = 123,
            scrap = true,
            comments = 123,
            policyId = "1",
            policyTitle = "폴리시 정책 이름",
        ),
        Post(
            postId = 1,
            title = "게시글 타이틀2",
            content = "게시글 내용",
            writerId = 12,
            scraps = 123,
            scrap = true,
            comments = 123,
            policyId = "1",
            policyTitle = "폴리시 정책 이름",
        ),
        Post(
            postId = 1,
            title = "게시글 타이틀3",
            content = "게시글 내용",
            writerId = 12,
            scraps = 123,
            scrap = true,
            comments = 123,
            policyId = "1",
            policyTitle = "폴리시 정책 이름",
        ),
        Post(
            postId = 1,
            title = "게시글 타이틀4",
            content = "게시글 내용",
            writerId = 12,
            scraps = 123,
            scrap = true,
            comments = 123,
            policyId = "1",
            policyTitle = "폴리시 정책 이름",
        ),
        Post(
            postId = 1,
            title = "게시글 타이틀5",
            content = "게시글 내용",
            writerId = 12,
            scraps = 123,
            scrap = true,
            comments = 123,
            policyId = "1",
            policyTitle = "폴리시 정책 이름",
        ),
        Post(
            postId = 1,
            title = "게시글 타이틀6",
            content = "게시글 내용",
            writerId = 12,
            scraps = 123,
            scrap = true,
            comments = 123,
            policyId = "1",
            policyTitle = "폴리시 정책 이름",
        ),
        Post(
            postId = 1,
            title = "게시글 타이틀7",
            content = "게시글 내용",
            writerId = 12,
            scraps = 123,
            scrap = true,
            comments = 123,
            policyId = "1",
            policyTitle = "폴리시 정책 이름",
        ),
    )
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
