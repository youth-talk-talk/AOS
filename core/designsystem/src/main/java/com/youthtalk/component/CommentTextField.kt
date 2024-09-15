package com.youthtalk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50

@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    text: String,
    isModifier: Boolean,
    onTextChange: (String) -> Unit,
    addComment: (String) -> Unit,
    modifierComment: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(8.dp),
            )
            .heightIn(max = 150.dp),
        value = text,
        textStyle = MaterialTheme.typography.titleMedium,
        onValueChange = onTextChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Default,
            keyboardType = KeyboardType.Text,
        ),
    ) { innerTextField ->

        Row(
            modifier = Modifier
                .padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                innerTextField()
                if (text.isEmpty()) {
                    Text(
                        text = if (isModifier) "수정할 댓글을 적어주세요" else "댓글을 달아보세요",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = gray50,
                        ),
                    )
                }
            }

            Icon(
                modifier = Modifier
                    .padding(vertical = 13.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        focusManager.clearFocus()
                        if (text.isNotEmpty()) {
                            if (isModifier) {
                                modifierComment(text)
                            } else {
                                addComment(text)
                            }
                        }
                    },
                painter = painterResource(id = R.drawable.arrow_up_icon),
                contentDescription = "댓글 등록",
                tint = Color.Black,
            )
        }
    }
}

@Preview
@Composable
private fun CommentScreenPreview() {
    YongProjectTheme {
        CommentTextField(
            addComment = {},
            modifierComment = {},
            text = "",
            onTextChange = {},
            isModifier = false,
        )
    }
}
