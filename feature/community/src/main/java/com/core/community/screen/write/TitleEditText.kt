package com.core.community.screen.write

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TitleEditText(textFieldState: TextFieldValue, onTitleTextChange: (TextFieldValue) -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp, start = 17.dp, end = 17.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            modifier = Modifier.width(42.dp),
            text = "제목",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        BasicTextField(
            textFieldState,
            onValueChange = onTitleTextChange,
            singleLine = true,
            modifier = Modifier
                .clearFocusOnKeyboardDismiss(),
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(horizontal = 13.dp, vertical = 17.dp),
            ) {
                if (textFieldState.text.isEmpty()) {
                    Text(
                        text = "제목을 작성해주세요",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
                innerTextField()
            }
        }
    }
}
