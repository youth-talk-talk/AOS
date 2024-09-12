package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.mypage.model.posts.MyPagePostsUiState
import com.core.mypage.viewmodel.MyPagePostViewModel
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.component.PostCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Post

@Composable
fun MyPagePostScreen(type: String, viewModel: MyPagePostViewModel, onBack: () -> Unit) {
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
            )
        }
    }
}

@Composable
private fun MyPagePosts(type: String, posts: LazyPagingItems<Post>, onBack: () -> Unit) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 12.dp),
        ) {
            items(
                count = posts.itemCount,
                key = { index -> posts.peek(index)?.policyId ?: "" },
            ) { index ->
                posts[index]?.let { post ->
                    PostCard(
                        policyTitle = post.policyTitle,
                        title = post.title,
                        scraps = post.scraps,
                        comments = post.comments,
                        scrap = post.scrap,
                        onClickScrap = {},
                    )
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
        )
    }
}
