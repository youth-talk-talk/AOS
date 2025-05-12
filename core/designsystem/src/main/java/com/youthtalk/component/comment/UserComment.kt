package com.youthtalk.component.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserComment(modifier: Modifier = Modifier, isMine: Boolean = false) {
    var bottomSheet by remember {
        mutableStateOf(false)
    }
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.profile_thumnail),
            contentDescription = "이미지"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "다른 청년",
                        style = MaterialTheme.typography.displayLarge
                    )

                    Text(
                        text = "3시간 전",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = gray80
                        )
                    )
                }
                Image(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            bottomSheet = true
                        },
                    painter = painterResource(R.drawable.more),
                    contentDescription = "더보기",
                    colorFilter = ColorFilter.tint(color = gray90)
                )
            }

            Text(
                text = "댓글 내용 댓글 내용 댓글 내용 댓글 내용 댓글 내용 댓글 내용 댓글 내용 ",
                style = MaterialTheme.typography.displaySmall
            )

            Row(
                modifier = Modifier
                    .padding(
                        top = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.favorite_line),
                        contentDescription = "좋아요",
                        colorFilter = ColorFilter.tint(color = gray70)
                    )
                    Text(
                        text = "좋아요",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = gray80
                        )
                    )
                }
                Text(
                    text = "답글쓰기",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80
                    )
                )
            }
        }

        if (bottomSheet) {
            ModalBottomSheet(
                sheetState = state,
                onDismissRequest = { bottomSheet = false }
            ) {
                val list = if (isMine) listOf("수정하기", "삭제하기") else listOf("댓글 신고하기", "사용자 차단하기")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 20.dp)
                ) {
                    list.forEach {
                        Text(
                            modifier = Modifier.padding(vertical = 14.dp),
                            text = it,
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                    HorizontalDivider(
                        color = gray40
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 14.dp),
                        text = "취소하기",
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UserCommentPreview() {
    YongProjectTheme {
        UserComment()
    }
}
