package com.youthtalk.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.component.tag.KeywordTag
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.comment.ArticleType
import com.youthtalk.model.comment.SettingComment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentCard(
    modifier: Modifier = Modifier,
    comment: SettingComment,
    isMine: Boolean = false,
    isMyType: Boolean,
    onClickModify: (SettingComment) -> Unit,
    onDeleteComment: (SettingComment) -> Unit,
    onClickLike: (Long, Boolean) -> Unit,
    onClickCard: (ArticleType, Long) -> Unit
) {
    var bottomSheet by remember {
        mutableStateOf(false)
    }
    var reportCommentDialog by remember {
        mutableStateOf(false)
    }
    var reportUserDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClickCard(comment.articleType, comment.articleId)
            }
            .background(color = gray10),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (!isMyType) {
                Text(
                    text = comment.nickname ?: "탈퇴한 회원",
                    style = MaterialTheme.typography.displayLarge
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = comment.content,
                    style = MaterialTheme.typography.displaySmall
                )

                Image(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            bottomSheet = true
                        },
                    painter = painterResource(R.drawable.more),
                    contentDescription = "더보기"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = gray50,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp, bottom = 11.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KeywordTag(
                text = when (comment.articleType) {
                    ArticleType.POST -> "자유게시판"
                    ArticleType.REVIEW -> "후기게시판"
                    ArticleType.POLICY -> "정책"
                }
            )

            Text(
                text = comment.articleTitle,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = gray90
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClickLike(comment.commentId, comment.isLikedByMember)
                },
                painter = painterResource(if (comment.isLikedByMember) R.drawable.favorite_fill else R.drawable.favorite_line),
                contentDescription = "좋아요",
                tint = if (comment.isLikedByMember) MaterialTheme.colorScheme.primary else gray80
            )

            Text(
                text = "${comment.likeCount}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = gray80
                )
            )
        }
    }

    if (bottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { bottomSheet = false },
            containerColor = Color(0xFDFFFFFF),
            contentColor = Color(0xFDFFFFFF)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                            if (isMine) {
                                onClickModify(comment)
                            } else {
                                reportCommentDialog = true
                            }
                            bottomSheet = false
                        }
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    text = if (isMine) "수정하기" else "댓글 신고하기",
                    style = MaterialTheme.typography.displaySmall
                )

                Text(
                    modifier = Modifier
                        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                            if (isMine) {
                                onDeleteComment(comment)
                            } else {
                                reportUserDialog = true
                            }
                            bottomSheet = false
                        }
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    text = if (isMine) "삭제하기" else "사용자 차단하기",
                    style = MaterialTheme.typography.displaySmall
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = gray40
                )
                Text(
                    modifier = Modifier.padding(vertical = 14.dp),
                    text = "취속하기",
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }

    if (reportUserDialog) {
        ModalDialog(
            title = "사용자를 차단할까요?",
            subTitle = "차단하면 ${comment.nickname}의 게시글과 댓글이 모두 보이지 않습니다.",
            confirmText = "차단하기",
            onDismissRequest = { reportCommentDialog = false },
            confirmBackground = MaterialTheme.colorScheme.error,
            onClickConfirm = {}
        )
    }

    if (reportCommentDialog) {
        ModalDialog(
            title = "댓글을 신고할까요?",
            confirmText = "신고하기",
            onDismissRequest = { reportCommentDialog = false },
            confirmBackground = MaterialTheme.colorScheme.error,
            onClickConfirm = {}
        )
    }
}

@Preview
@Composable
private fun CommentCardPreview() {
    YongProjectTheme {
        CommentCard(
            comment = SettingComment(
                commentId = 2123,
                writerId = 1847,
                nickname = null,
                content = "ignota",
                articleId = 1883,
                articleType = ArticleType.REVIEW,
                articleTitle = "per",
                isLikedByMember = false,
                likeCount = 8960
            ),
            isMine = true,
            isMyType = false,
            onClickModify = {},
            onDeleteComment = {},
            onClickLike = { _, _ -> },
            onClickCard = { _, _ -> }
        )
    }
}

@Preview
@Composable
private fun CommentCardIsMinePreview() {
    YongProjectTheme {
        CommentCard(
            comment = SettingComment(
                commentId = 2123,
                writerId = 1847,
                nickname = null,
                content = "ignota",
                articleId = 1883,
                articleType = ArticleType.REVIEW,
                articleTitle = "per",
                isLikedByMember = false,
                likeCount = 8960
            ),
            isMine = true,
            isMyType = true,
            onClickModify = {},
            onDeleteComment = {},
            onClickLike = { _, _ -> },
            onClickCard = { _, _ -> }
        )
    }
}
