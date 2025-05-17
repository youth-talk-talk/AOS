package com.youthtalk.component.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.Comment
import com.youthtalk.util.getTime
import java.time.LocalDateTime
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserComment(
    modifier: Modifier = Modifier,
    comment: Comment,
    isMine: Boolean = false,
    onPostModifyComment: (Comment) -> Unit,
    onDeleteComment: (Comment) -> Unit,
    onPostReportComment: () -> Unit,
    onPostReportUser: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var bottomSheet by remember {
        mutableStateOf(false)
    }
    val dateTime by remember {
        mutableStateOf(comment.createdAt.getTime())
    }
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        comment.profileImg?.let { url ->
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = url,
                contentDescription = null
            )
        } ?: Image(
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
                        text = comment.nickname,
                        style = MaterialTheme.typography.displayLarge
                    )

                    Text(
                        text = dateTime,
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
                text = comment.content,
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
                        painter = painterResource(if (comment.isLikedByMember) R.drawable.favorite_fill else R.drawable.favorite_line),
                        contentDescription = "좋아요",
                        colorFilter = ColorFilter.tint(color = if (comment.isLikedByMember) MaterialTheme.colorScheme.primary else gray70)
                    )
                    Text(
                        text = "좋아요",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = gray80
                        )
                    )
                }
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
                    list.forEachIndexed { index, text ->
                        Text(
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    scope.launch {
                                        state.hide()
                                        bottomSheet = false
                                    }
                                    if (index == 0) {
                                        if (isMine) onPostModifyComment(comment) else onPostReportComment()
                                    } else {
                                        if (isMine) onDeleteComment(comment) else onPostReportUser()
                                    }
                                }
                                .padding(vertical = 14.dp),
                            text = text,
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
        UserComment(
            comment = Comment(
                commentId = 4615,
                writerId = 9887,
                nickname = "Joey Davidson",
                content = "viderer",
                isLikedByMember = false,
                profileImg = null,
                createdAt = LocalDateTime.now()
            ),
            onPostModifyComment = {},
            onDeleteComment = {},
            onPostReportComment = {},
            onPostReportUser = {}
        )
    }
}
