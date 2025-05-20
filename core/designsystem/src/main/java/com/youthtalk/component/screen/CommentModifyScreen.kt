package com.youthtalk.component.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray70

@Composable
fun CommentModifyScreen(
    modifier: Modifier = Modifier,
    comment: String,
    onPostCommentModify: () -> Unit,
    onBack: () -> Unit,
    onTextChange: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        MiddleTitleTopBar(
            onBack = onBack,
            title = "댓글 수정",
            tails = {
                Text(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onPostCommentModify()
                    },
                    text = "등록",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (comment.isNotEmpty()) gray100 else gray70
                    )
                )
            }
        )

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            value = comment,
            onValueChange = onTextChange,
            textStyle = MaterialTheme.typography.displaySmall
        ) { innerTextField ->
            innerTextField()
        }
    }
}
