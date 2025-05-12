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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.component.tag.KeywordTag
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentCard(modifier: Modifier = Modifier, isMine: Boolean = false) {
    var bottomSheet by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = gray10),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (!isMine) {
                Text(
                    text = "User",
                    style = MaterialTheme.typography.displayLarge
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = "댓글 내용 댓글 내용 댓글 내용 댓글 내용 댓글 내용 댓글 내용 댓글 내용 댓글 내용 댓글 내용 댓글 내용",
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
                text = "자유게시판"
            )

            Text(
                text = "글 제목",
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
                painter = painterResource(R.drawable.favorite_line),
                contentDescription = "좋아요",
                tint = gray80
            )

            Text(
                text = "0",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = gray80
                )
            )
        }
    }

    if (bottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { bottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 14.dp),
                    text = if (isMine) "수정하기" else "댓글 신고하기",
                    style = MaterialTheme.typography.displaySmall
                )

                Text(
                    modifier = Modifier.padding(vertical = 14.dp),
                    text = if (isMine) "수정하기" else "사용자 차단하기",
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
}

@Preview
@Composable
private fun CommentCardPreview() {
    YongProjectTheme {
        CommentCard()
    }
}

@Preview
@Composable
private fun CommentCardIsMinePreview() {
    YongProjectTheme {
        CommentCard(
            isMine = true
        )
    }
}
