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
import com.core.mypage.model.comments.MyPageCommentsUiState
import com.core.mypage.viewmodel.MyPageCommentsViewModel
import com.youthtalk.component.CommentScreen
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Comment
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MyPageCommentScreen(isMine: Boolean, viewModel: MyPageCommentsViewModel, onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is MyPageCommentsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is MyPageCommentsUiState.Success -> {
            val state = uiState as MyPageCommentsUiState.Success
            val comments = state.comments
            MyPageComments(
                isMine = isMine,
                comments = comments,
                onBack = onBack,
            )
        }
    }
}

@Composable
private fun MyPageComments(isMine: Boolean, comments: ImmutableList<Comment>, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        MiddleTitleTopBar(
            title = if (isMine) "작성한 댓글" else "좋아요한 댓글",
            onBack = onBack,
        )
        HorizontalDivider()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 12.dp),
        ) {
            items(
                count = comments.size,
                key = { index -> comments[index].commentId },
            ) { index ->
                val comment = comments[index]
                CommentScreen(
                    nickname = comment.nickname,
                    content = comment.content,
                    isLike = comment.isLikedByMember,
                    isMine = isMine,
                    deleteComment = {},
                )
            }
        }
    }
}

private fun getComments(): List<Comment> {
    val list = mutableListOf<Comment>()
    repeat(30) { index ->
        list.add(
            Comment(
                commentId = 0,
                nickname = "닉네임${index + 1}",
                content = "컨텐츠${index + 1}",
                isLikedByMember = true,
            ),
        )
    }
    return list
}

@Preview
@Composable
private fun MyPageCommentScreenPreview() {
    val viewModel: MyPageCommentsViewModel = hiltViewModel()
    YongProjectTheme {
        MyPageCommentScreen(
            isMine = true,
            viewModel = viewModel,
            onBack = {},
        )
    }
}
