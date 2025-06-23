package com.core.mypage.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.mypage.model.comment.CommentUiEffect
import com.core.mypage.model.comment.CommentUiEvent
import com.core.mypage.model.comment.CommentUiState
import com.core.mypage.model.comment.SettingCommentScreenType
import com.core.mypage.viewmodel.CommentViewModel
import com.core.navigation.model.CommentType
import com.youth.app.feature.mypage.R
import com.youthtalk.component.card.CommentCard
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.screen.CommentModifyScreen
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray40
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommentScreenRoot(
    viewModel: CommentViewModel = hiltViewModel(),
    onBack: () -> Unit,
    showSnackBar: (String) -> Unit,
    onClickPostDetail: (Long) -> Unit,
    onClickPolicyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var comments by rememberSaveable {
        mutableStateOf(Pair(0L, ""))
    }
    var isRefresh by rememberSaveable {
        mutableStateOf(false)
    }
    val lazyListState = rememberLazyListState()

    LifecycleResumeEffect(Unit) {
        if (isRefresh) {
            viewModel.setEvent(CommentUiEvent.RefreshData)
            isRefresh = false
        }
        onPauseOrDispose {
            isRefresh = true
        }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is CommentUiEffect.ModifyInfo -> comments = Pair(it.commentId, it.content)
                is CommentUiEffect.ShowSnackBar -> showSnackBar(it.message)
                is CommentUiEffect.DetailPolicy -> onClickPolicyDetail(it.policyId)
                is CommentUiEffect.DetailPost -> onClickPostDetail(it.postId)
            }
        }
    }
    if (!state.isLoading) {
        Crossfade(
            modifier = modifier,
            targetState = state.commentScreenType
        ) {
            when (it) {
                SettingCommentScreenType.MAIN -> {
                    CommentScreen(
                        state = state,
                        lazyListState = lazyListState,
                        actionEvent = viewModel::setEvent,
                        onBack = onBack
                    )
                }

                SettingCommentScreenType.COMMENT -> CommentModifyScreen(
                    comment = comments.second,
                    onBack = { viewModel.setEvent(CommentUiEvent.OnBackMain) },
                    onPostCommentModify = { viewModel.setEvent(CommentUiEvent.OnPatchComment(comments.first, comments.second)) },
                    onTextChange = { text -> comments = comments.copy(second = text) }
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CommentScreen(
    state: CommentUiState,
    lazyListState: LazyListState,
    actionEvent: (CommentUiEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        NoBackMiddleTitleTopBar(
            title = when (state.commentType) {
                CommentType.MY -> "내 댓글"
                CommentType.LIKE -> "좋아요한 댓글"
            },
            tails = {
                Image(
                    modifier = Modifier
                        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onBack() },
                    painter = painterResource(R.drawable.close),
                    contentDescription = stringResource(R.string.close)
                )
            }
        )

        if (state.commentInfo.commentCount != 0) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                state = lazyListState
            ) {
                item {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 14.dp),
                        text = "댓글 ${state.commentInfo.commentCount}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                items(
                    items = state.commentInfo.comments,
                    key = { it.commentId }
                ) { comment ->
                    CommentCard(
                        isMyType = state.commentType == CommentType.MY,
                        isMine = state.commentType == CommentType.MY || (comment.writerId == state.user.memberId),
                        comment = comment,
                        onClickModify = { modify -> actionEvent(CommentUiEvent.OnClickModify(modify)) },
                        onDeleteComment = { delete -> actionEvent(CommentUiEvent.OnDeleteComment(delete)) },
                        onClickLike = { commentId, isLike -> actionEvent(CommentUiEvent.PostCommentLike(commentId, isLike)) },
                        onClickCard = { type, id -> actionEvent(CommentUiEvent.OnClickCard(type, id)) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = gray40,
                        thickness = 1.dp
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.weight(2f))
                EmptyScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f),
                    emptyTitle = if (state.commentType == CommentType.MY) {
                        "작성한 댓글이 없습니다."
                    } else {
                        "좋아요한 댓글이 없습니다."
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommentScreenMyPreview() {
    YongProjectTheme {
        val state = rememberLazyListState()
        CommentScreen(
            lazyListState = state,
            state = CommentUiState.initState,
            actionEvent = {},
            onBack = {}
        )
    }
}

@Preview
@Composable
private fun CommentScreenLikePreview() {
    YongProjectTheme {
        val state = rememberLazyListState()
        CommentScreen(
            state = CommentUiState.initState,
            lazyListState = state,
            actionEvent = {},
            onBack = {}
        )
    }
}
