package com.feature.policydetail.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.comment.UserComment
import com.youthtalk.model.Comment
import java.time.LocalDateTime

fun LazyListScope.policyFooter() {
    items(
        count = 10
    ) {
        if (it == 0) {
            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 20.dp,
                    top = 16.dp
                ),
                text = "댓글 7",
                style = MaterialTheme.typography.displayLarge
            )
        }

        UserComment(
            comment = Comment(
                commentId = 0,
                writerId = 0,
                nickname = "",
                content = "",
                isLikedByMember = false,
                profileImg = null,
                createdAt = LocalDateTime.now()
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            onPostReportUser = {},
            onDeleteComment = {},
            onPostReportComment = {},
            onPostModifyComment = {}
        )
    }
}
