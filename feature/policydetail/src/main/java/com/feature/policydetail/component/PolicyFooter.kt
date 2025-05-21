package com.feature.policydetail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.comment.UserComment
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.model.User
import com.youthtalk.model.comment.Comment
import com.youthtalk.model.comment.CommentInfo

fun LazyListScope.policyFooter(commentInfo: CommentInfo, user: User, onPostModifyComment: (Comment) -> Unit, onDeleteComment: (Comment) -> Unit) {
    item {
        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 20.dp,
                top = 16.dp
            ),
            text = "댓글 ${commentInfo.commentCount}",
            style = MaterialTheme.typography.displayLarge
        )
    }

    if (commentInfo.commentCount != 0) {
        items(
            count = commentInfo.commentCount
        ) {
            UserComment(
                comment = commentInfo.comments[it],
                isMine = user.memberId == commentInfo.comments[it].writerId,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                onDeleteComment = onDeleteComment,
                onPostReportComment = {},
                onPostModifyComment = onPostModifyComment,
                onPostReportUser = {}
            )
        }
    } else {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyScreen(
                    modifier = Modifier
                        .fillMaxWidth(),
                    emptyTitle = "아직 댓글이 없어요.\n가장 먼저 댓글을 남겨보세요."
                )
            }
        }
    }
}
