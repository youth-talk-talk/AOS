package com.youthtalk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun CommentScreen(
    modifier: Modifier = Modifier,
    nickname: String,
    content: String,
    isLike: Boolean,
    isMine: Boolean,
    isDetailScreen: Boolean = true,
    deleteComment: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(
                Color.White,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(
                horizontal = 13.dp,
                vertical = 10.dp,
            ),
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = nickname,
                style = MaterialTheme.typography.displayMedium,
            )

            Text(
                text = content,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.W400,
                ),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight(),
        ) {
            if (isMine && isDetailScreen) {
                Row(
                    modifier = Modifier.align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    Text(
                        modifier = Modifier.width(21.dp),
                        text = stringResource(id = R.string.modify),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                    Text(
                        modifier = Modifier
                            .width(21.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                deleteComment()
                            },
                        text = stringResource(id = R.string.remove),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
            }

            if (!isMine) {
                Icon(
                    modifier = Modifier
                        .padding(start = 29.dp)
                        .align(Alignment.CenterEnd),
                    painter = if (isLike) painterResource(id = R.drawable.favorite_check_icon) else painterResource(id = R.drawable.favorite_icon),
                    contentDescription = stringResource(id = R.string.favorite),
                    tint = if (isLike) MaterialTheme.colorScheme.primary else Color.Black,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommentScreenIsMinePreview() {
    YongProjectTheme {
        CommentScreen(
            nickname = "놀고픈 청년",
            content = "댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다",
            isLike = true,
            isMine = true,
            deleteComment = {},
        )
    }
}

@Preview
@Composable
private fun CommentScreenOtherPreview() {
    YongProjectTheme {
        CommentScreen(
            nickname = "놀고픈 청년",
            content = "댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다댓글내용입니다",
            isMine = false,
            isLike = true,
            deleteComment = {},
        )
    }
}
