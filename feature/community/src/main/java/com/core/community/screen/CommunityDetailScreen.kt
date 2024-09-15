package com.core.community.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.core.community.model.CommunityDetailUiEvent
import com.core.community.model.CommunityDetailUiState
import com.core.community.viewmodel.CommunityDetailViewModel
import com.youth.app.feature.community.R
import com.youthtalk.component.CommentScreen
import com.youthtalk.component.CommentTextField
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Comment
import com.youthtalk.model.PostDetail
import com.youthtalk.model.User

@Composable
fun CommunityDetailScreen(postId: Long, viewModel: CommunityDetailViewModel = hiltViewModel(), onBack: () -> Unit) {
    LifecycleEventEffect(
        event = Lifecycle.Event.ON_CREATE,
    ) {
        viewModel.uiEvent(CommunityDetailUiEvent.GetPostDetail(postId))
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            CommunityDetail(
                post = state.post,
                user = state.user,
                comments = state.comments,
                onBack = onBack,
                onClickLike = { id, isLike -> viewModel.uiEvent(CommunityDetailUiEvent.PostCommentLike(id, isLike)) },
                onAddComment = { id, text -> viewModel.uiEvent(CommunityDetailUiEvent.PostAddComment(id, text)) },
                onDeleteComment = { index, commentId -> viewModel.uiEvent(CommunityDetailUiEvent.DeleteComment(index, commentId)) },
                onModifyComment = { id, content -> viewModel.uiEvent(CommunityDetailUiEvent.ModifyComment(id, content)) },
            )
        }
    }
}

@Composable
private fun CommunityDetail(
    post: PostDetail,
    user: User,
    comments: List<Comment>,
    onBack: () -> Unit,
    onClickLike: (Long, Boolean) -> Unit,
    onAddComment: (Long, String) -> Unit,
    onDeleteComment: (Int, Long) -> Unit,
    onModifyComment: (Long, String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
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
            .background(MaterialTheme.colorScheme.background),
    ) {
        CommunityDetailAppBar(category = post.category, onBack = onBack, isMine = user.memberId == post.writerId)
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 15.dp),
        ) {
            item {
                CommunityTitle(
                    modifier = Modifier.padding(bottom = 12.dp),
                    nickname = post.nickname ?: "탈퇴한 회원",
                    title = post.title,
                    policyTitle = post.policyTitle,
                )
            }

            if (post.content.isNotEmpty()) {
                item {
                    Text(
                        text = post.content,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
            }

            if (post.contentList.isNotEmpty()) {
                items(
                    count = post.contentList.size,
                    key = { index -> post.contentList[index] },
                ) { index ->
                    val content = post.contentList[index]
                    if (content.type == "IMAGE") {
                        AsyncImage(
                            model = content.content,
                            contentDescription = null,
                        )
                    } else {
                        Text(
                            text = content.content,
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "댓글",
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Text(
                        text = "${comments.size}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
            }

            items(
                count = comments.size,
            ) { index ->
                val comment = comments[index]
                CommentScreen(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .shadow(
                            elevation = 3.dp,
                        ),
                    nickname = comment.nickname,
                    content = comment.content,
                    isLike = comment.isLikedByMember,
                    isMine = user.nickname == comment.nickname,
                    deleteComment = { onDeleteComment(index, comment.commentId) },
                    modifierComment = {
                        text = it
                        modifier = Pair(comment.commentId, true)
                        focusRequester.requestFocus()
                    },
                    onClickLike = { onClickLike(comment.commentId, comment.isLikedByMember) },
                )
            }
        }

        CommentTextField(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 18.dp)
                .focusRequester(focusRequester)
                .imePadding(),
            text = text,
            isModifier = modifier.second,
            onTextChange = onTextChange,
            addComment = {
                onAddComment(post.postId, it)
                text = ""
            },
            modifierComment = {
                onModifyComment(modifier.first, it)
                text = ""
                modifier = Pair(-1, false)
            },
        )
    }
}

private fun getComments() = listOf(
    Comment(
        commentId = 142,
        nickname = "집가고싶다어",
        content = "댓글내용수정1",
        isLikedByMember = false,
        postId = 0,
        policyId = "",
    ),
    Comment(
        commentId = 144,
        nickname = "집가고싶다어",
        content = "댓글내용수정3",
        isLikedByMember = false,
        postId = 0,
        policyId = "",
    ),
    Comment(
        commentId = 382,
        nickname = "집가고싶다어",
        content = "댓글내용수정2",
        isLikedByMember = false,
        postId = 0,
        policyId = "",
    ),
)

@Composable
fun CommunityTitle(modifier: Modifier = Modifier, nickname: String, title: String, policyTitle: String?) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = nickname,
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )

        policyTitle?.let { policyTitle ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "정책명",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondary,
                    ),
                )
                Text(
                    text = policyTitle,
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = MaterialTheme.colorScheme.onTertiary,
                    ),
                )
            }
        }
    }
}

@Composable
fun CommunityDetailAppBar(modifier: Modifier = Modifier, category: String?, isMine: Boolean, onBack: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .padding(
                horizontal = 17.dp,
                vertical = 13.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onBack() },
                painter = painterResource(id = R.drawable.left_icon),
                contentDescription = "뒤로가기",
                tint = Color.Black,
            )
            category?.let { categoryName ->
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    ),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.share_icon),
                contentDescription = "공유",
                tint = Color.Black,
            )

            Icon(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "북마크",
                tint = Color.Black,
            )

            if (isMine) {
                Icon(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            expanded = true
                        },
                    painter = painterResource(id = R.drawable.more_icon),
                    contentDescription = "더보기",
                    tint = Color.Black,
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("수정") },
                onClick = { /* Handle edit! */ },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("삭제") },
                onClick = { /* Handle settings! */ },
            )
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
        )
    }
}
