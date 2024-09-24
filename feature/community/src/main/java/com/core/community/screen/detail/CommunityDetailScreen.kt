package com.core.community.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.community.model.CommunityDetailUiEffect
import com.core.community.model.CommunityDetailUiEvent
import com.core.community.model.CommunityDetailUiState
import com.core.community.viewmodel.CommunityDetailViewModel
import com.youthtalk.component.CustomDialog
import com.youthtalk.designsystem.YongProjectTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommunityDetailScreen(
    postId: Long,
    viewModel: CommunityDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    goWriteScreen: (Long, String) -> Unit,
) {
    LifecycleEventEffect(
        event = Lifecycle.Event.ON_CREATE,
    ) {
        viewModel.uiEvent(CommunityDetailUiEvent.GetPostDetail(postId))
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collectLatest {
            when (it) {
                is CommunityDetailUiEffect.CommunityWrite -> goWriteScreen(it.id, it.type)
            }
        }
    }
    when (uiState) {
        is CommunityDetailUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is CommunityDetailUiState.Success -> {
            val state = uiState as CommunityDetailUiState.Success
            var deleteDialog by remember { mutableStateOf(false) }

            CommunityDetail(
                post = state.post,
                user = state.user,
                comments = state.comments,
                onBack = onBack,
                onClickLike = { id, isLike -> viewModel.uiEvent(CommunityDetailUiEvent.PostCommentLike(id, isLike)) },
                onAddComment = { id, text -> viewModel.uiEvent(CommunityDetailUiEvent.PostAddComment(id, text)) },
                onDeleteComment = { index, commentId -> viewModel.uiEvent(CommunityDetailUiEvent.DeleteComment(index, commentId)) },
                onModifyComment = { id, content -> viewModel.uiEvent(CommunityDetailUiEvent.ModifyComment(id, content)) },
                onDeleteDialog = { deleteDialog = true },
                onClickModifier = { viewModel.uiEvent(CommunityDetailUiEvent.ModifyPost) },
                onPostScrap = { id, scrap -> viewModel.uiEvent(CommunityDetailUiEvent.PostScrap(id, scrap)) },
            )

            if (deleteDialog) {
                CustomDialog(
                    title = "게시물을 삭제하시겠습니까?",
                    onCancel = { deleteDialog = false },
                    onSuccess = { /*TODO*/ },
                    onDismiss = { deleteDialog = false },
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommunityDetailScreenPreview() {
    YongProjectTheme {
        CommunityDetailScreen(
            postId = 0,
            onBack = {},
            goWriteScreen = { _, _ -> },
        )
    }
}
