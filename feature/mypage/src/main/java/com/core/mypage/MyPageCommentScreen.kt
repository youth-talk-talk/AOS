package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
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
import com.core.mypage.model.comments.MyPageCommentsUiEvent
import com.core.mypage.model.comments.MyPageCommentsUiState
import com.core.mypage.viewmodel.MyPageCommentsViewModel
import com.youthtalk.component.CommentScreen
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Comment
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MyPageCommentScreen(
    isMine: Boolean,
    viewModel: MyPageCommentsViewModel = hiltViewModel(),
    onBack: () -> Unit,
    policyDetail: (String) -> Unit,
    postDetail: (Long) -> Unit,
) {
    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.uiEvent(MyPageCommentsUiEvent.GetData(isMine))
    }
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

            LifecycleResumeEffect(Unit) {
                viewModel.uiEvent(MyPageCommentsUiEvent.GetData(isMine))
                onPauseOrDispose { }
            }

            MyPageComments(
                isMine = isMine,
                comments = comments,
                onBack = onBack,
                policyDetail = policyDetail,
                postDetail = postDetail,
                onClickLike = { id, scrap -> viewModel.uiEvent(MyPageCommentsUiEvent.CommentLike(id, scrap)) },
            )
        }
    }
}

@Composable
private fun MyPageComments(
    isMine: Boolean,
    comments: ImmutableList<Comment>,
    onBack: () -> Unit,
    policyDetail: (String) -> Unit,
    postDetail: (Long) -> Unit,
    onClickLike: (Long, Boolean) -> Unit,
) {
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
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null,
                        ) {
                            comment.postId?.let(postDetail)
                            comment.policyId?.let(policyDetail)
                        },
                    nickname = comment.nickname,
                    content = comment.content,
                    isLike = comment.isLikedByMember,
                    isMine = isMine,
                    isDetailScreen = false,
                    onClickLike = { onClickLike(comment.commentId, comment.isLikedByMember) },
                )
            }
        }
    }
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
            postDetail = {},
            policyDetail = {},
        )
    }
}
