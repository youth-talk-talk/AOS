package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.mypage.component.account.AccountTopBar
import com.youthtalk.component.PostCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Post

@Composable
fun MyPagePostScreen(isMine: Boolean) {
    val getPosts = getPost()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        AccountTopBar(title = "스크랩한 게시물")
        HorizontalDivider()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 12.dp),
        ) {
            items(
                count = getPosts.size,
            ) { index ->
                val post = getPosts[index]
                PostCard(
                    policyTitle = post.policyTitle,
                    title = post.title,
                    scraps = post.scraps,
                    comments = post.comments,
                    scrap = post.scrap,
                )
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
    YongProjectTheme {
        MyPagePostScreen(
            isMine = true,
        )
    }
}
