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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100

@Composable
fun Round6Button(modifier: Modifier = Modifier, text: String, textColor: Color = gray100, backgroundColor: Color = gray10, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = textColor
            )
        )
    }
}

@Preview
@Composable
private fun Round6ButtonColorPreview() {
    YongProjectTheme {
        Round6Button(
            text = "삭제하기",
            textColor = gray10,
            backgroundColor = MaterialTheme.colorScheme.error,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun Round6ButtonPreview() {
    YongProjectTheme {
        Round6Button(
            text = "닫기",
            onClick = {}
        )
    }
}
