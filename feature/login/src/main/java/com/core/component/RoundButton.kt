package com.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun RoundButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
) {
    Box(
        modifier =
            modifier
                .background(color, shape = RoundedCornerShape(48.dp))
                .padding(vertical = 16.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun RoundButtonNoPreview() {
    YongProjectTheme(darkTheme = false) {
        RoundButton(
            text = "아니요",
            color = MaterialTheme.colorScheme.surface,
        )
    }
}

@Preview
@Composable
private fun RoundButtonYesPreview() {
    YongProjectTheme(darkTheme = false) {
        RoundButton(
            text = "예",
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
