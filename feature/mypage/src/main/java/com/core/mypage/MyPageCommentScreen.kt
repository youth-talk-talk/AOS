package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.component.CommentScreen
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Comment

@Composable
fun MyPageCommentScreen(isMine: Boolean, onBack: () -> Unit) {
    val comments = getComments()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        MiddleTitleTopBar(
            title = "좋아요한 댓글",
            onBack = onBack,
        )
        HorizontalDivider()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 12.dp),
        ) {
            items(
                count = comments.size,
            ) { index ->
                val comment = comments[index]
                CommentScreen(
                    nickname = comment.nickname,
                    content = comment.content,
                    isLike = comment.isLikedByMember,
                    isMine = false,
                    deleteComment = {},
                )
            }
        }
    }
}

private fun getComments(): List<Comment> {
    val list = mutableListOf<Comment>()
    repeat(30) { index ->
        list.add(
            Comment(
                commentId = 0,
                nickname = "닉네임${index + 1}",
                content = "컨텐츠${index + 1}",
                isLikedByMember = true,
            ),
        )
    }
    return list
}

@Preview
@Composable
private fun MyPageCommentScreenPreview() {
    YongProjectTheme {
        MyPageCommentScreen(
            isMine = true,
            onBack = {},
        )
    }
}
