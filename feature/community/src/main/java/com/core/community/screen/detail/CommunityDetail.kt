package com.core.community.screen.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.youthtalk.component.CommentScreen
import com.youthtalk.component.CommentTextField
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Comment
import com.youthtalk.model.PostDetail
import com.youthtalk.model.Region
import com.youthtalk.model.User

@Composable
fun CommunityDetail(
    post: PostDetail,
    user: User,
    comments: List<Comment>,
    onBack: () -> Unit,
    onClickLike: (Long, Boolean) -> Unit,
    onAddComment: (Long, String) -> Unit,
    onDeleteComment: (Int, Long) -> Unit,
    onModifyComment: (Long, String) -> Unit,
    onDeleteDialog: () -> Unit,
    onClickModifier: () -> Unit,
    onPostScrap: (Long, Boolean) -> Unit,
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
        CommunityDetailAppBar(
            category = post.category,
            onBack = onBack,
            isMine = user.memberId == post.writerId,
            scrap = post.scrap,
            onDeleteDialog = onDeleteDialog,
            onClickModifier = onClickModifier,
            onPostScrap = { onPostScrap(post.postId, it) },
        )
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

@Preview
@Composable
private fun CommunityDetailPreview() {
    YongProjectTheme {
        CommunityDetail(
            post = PostDetail(
                postId = 2057,
                postType = "accusata",
                title = "nibh",
                content = "wisi",
                contentList = listOf(),
                policyId = null,
                policyTitle = null,
                writerId = 2980,
                nickname = null,
                view = 5310,
                images = listOf(),
                category = null,
                scrap = false,
            ),
            user = User(
                memberId = 7910,
                nickname = "Katina Luna",
                region = Region.CHUNGBUK,
            ),
            comments = listOf(),
            onBack = { },
            onClickLike = { _, _ -> },
            onAddComment = { _, _ -> },
            onDeleteComment = { _, _ -> },
            onModifyComment = { _, _ -> },
            onDeleteDialog = { },
            onClickModifier = { },
            onPostScrap = { _, _ -> },
        )
    }
}
