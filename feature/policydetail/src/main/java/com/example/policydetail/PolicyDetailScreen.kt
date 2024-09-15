package com.example.policydetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.policydetail.component.PolicyDetail
import com.example.policydetail.component.PolicyDetailTopAppBar
import com.example.policydetail.component.PolicyTitle
import com.example.policydetail.model.PolicyDetailUiState
import com.youth.app.feature.policydetail.R
import com.youthtalk.component.CommentScreen
import com.youthtalk.component.CommentTextField
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50
import com.youthtalk.model.Comment
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PolicyDetailScreen(policyId: String, viewModel: PolicyDetailViewModel = hiltViewModel(), onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        if (uiState is PolicyDetailUiState.Loading) {
            viewModel.getData(policyId)
        }
    }
    if (uiState !is PolicyDetailUiState.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        PolicyDetailSuccessScreen(
            uiState as PolicyDetailUiState.Success,
            onBack = onBack,
            onClickScrap = { viewModel.postScrap(policyId, it) },
            addComment = { viewModel.addComment(policyId, it) },
            deleteComment = { index, id -> viewModel.deleteComment(index, id) },
            onModifyComment = { id, content -> viewModel.modifyComment(id, content) },
            postCommentLike = { id, isLike -> viewModel.postCommentLike(id, isLike) },
        )
    }
}

@Composable
private fun PolicyDetailSuccessScreen(
    uiState: PolicyDetailUiState.Success,
    onBack: () -> Unit,
    onClickScrap: (Boolean) -> Unit,
    addComment: (String) -> Unit,
    deleteComment: (Int, Long) -> Unit,
    onModifyComment: (Long, String) -> Unit,
    postCommentLike: (Long, Boolean) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val commentTextFieldRequest = remember { FocusRequester() }
    val policyDetail = uiState.policyDetail
    val comments = uiState.comments
    val user = uiState.myInfo
    var text by remember { mutableStateOf("") }
    var modifier by remember { mutableStateOf(Pair(-1L, false)) }
    val onTextChange: (String) -> Unit = { text = it }

    BackHandler {
        if (modifier.second) {
            modifier = Pair(-1, false)
            focusManager.clearFocus()
            text = ""
        } else {
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        PolicyDetailTopAppBar(
            isScrap = policyDetail.isScrap,
            onClickScrap = onClickScrap,
            onBack = onBack,

        )

        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            item {
                PolicyTitle(
                    modifier = Modifier
                        .padding(start = 17.dp, end = 22.dp, top = 4.dp),
                    topTitle = policyDetail.hostDep,
                    mainTitle = policyDetail.title,
                    titleDescription = policyDetail.introduction,
                )
            }
            item {
                PolicyDetail(
                    modifier = Modifier
                        .padding(top = 29.dp)
                        .padding(horizontal = 17.dp),
                    policyDetail = policyDetail,
                )
            }

            item {
                RoundButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 17.dp,
                            vertical = 12.dp,
                        ),
                    text = stringResource(id = R.string.apply_btn_name),
                    color = MaterialTheme.colorScheme.primary,
                    onClick = {},
                )
            }

            item {
                CommentCountText(comments)
            }

            items(
                count = comments.size,
            ) { index: Int ->
                CommentScreen(
                    modifier = Modifier
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp)
                        .shadow(3.dp),
                    nickname = comments[index].nickname,
                    content = comments[index].content,
                    isLike = comments[index].isLikedByMember,
                    isMine = user.nickname == comments[index].nickname,
                    deleteComment = { deleteComment(index, comments[index].commentId) },
                    onClickLike = { postCommentLike(comments[index].commentId, comments[index].isLikedByMember) },
                    modifierComment = {
                        text = it
                        modifier = Pair(comments[index].commentId, true)
                        commentTextFieldRequest.requestFocus()
                    },
                )
            }
        }

        CommentTextField(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 18.dp)
                .focusRequester(commentTextFieldRequest)
                .imePadding(),
            text = text,
            onTextChange = onTextChange,
            isModifier = modifier.second,
            addComment = addComment,
            modifierComment = {
                onModifyComment(modifier.first, it)
                text = ""
                modifier = Pair(-1, false)
            },
        )
    }
}

@Composable
private fun CommentCountText(comments: ImmutableList<Comment>) {
    Row(
        modifier = Modifier
            .padding(start = 17.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "댓글",
            style = MaterialTheme.typography.displayLarge,
        )
        Text(
            text = comments.size.toString(),
            style = MaterialTheme.typography.titleSmall.copy(
                color = gray50,
            ),
        )
    }
}

@Preview
@Composable
private fun PolicyDetailScreenPreview() {
    YongProjectTheme {
        PolicyDetailScreen(
            policyId = "1234",
            onBack = {},
        )
    }
}
