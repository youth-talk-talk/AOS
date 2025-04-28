package com.youthtalk.component.tag

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
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray80

@Composable
fun KeywordTag(modifier: Modifier = Modifier, text: String, backgroundColor: Color = gray30, textColor: Color = gray80) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                color = textColor,
            ),
        )
    }
}

@Preview
@Composable
private fun KeywordTagPreview() {
    YongProjectTheme {
        KeywordTag(
            text = "상시",
            textColor = MaterialTheme.colorScheme.errorContainer,
            backgroundColor = MaterialTheme.colorScheme.onError,
        )
    }
}

@Preview
@Composable
private fun KeywordTagRegionPreview() {
    YongProjectTheme {
        KeywordTag(
            text = "지역",
        )
    }
}
