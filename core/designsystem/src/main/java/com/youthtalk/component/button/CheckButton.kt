package com.youthtalk.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray70

@Composable
fun CheckButton(modifier: Modifier = Modifier, isCheck: Boolean = false, text: String, onClick: () -> Unit) {
    val backgroundColor = if (isCheck) MaterialTheme.colorScheme.primary else gray30
    val titleColor = if (isCheck) gray10 else gray70
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp),
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                if (isCheck) onClick()
            }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = titleColor,
            ),
        )
    }
}

@Preview
@Composable
private fun CheckButtonTruePreview() {
    YongProjectTheme {
        CheckButton(
            modifier = Modifier.fillMaxWidth(),
            isCheck = true,
            text = "다음",
        ) { }
    }
}

@Preview
@Composable
private fun CheckButtonFalsePreview() {
    YongProjectTheme {
        CheckButton(
            modifier = Modifier.fillMaxWidth(),
            isCheck = false,
            text = "다음",
        ) { }
    }
}
