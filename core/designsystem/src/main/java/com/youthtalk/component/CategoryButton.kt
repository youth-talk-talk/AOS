package com.youthtalk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun CategoryButton(modifier: Modifier = Modifier, title: String, isSelected: Boolean) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onTertiary,
                shape = RoundedCornerShape(10.dp),
            )
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Preview
@Composable
private fun CategoryButtonTruePreview() {
    YongProjectTheme {
        CategoryButton(
            title = "전체선택",
            isSelected = true,
        )
    }
}

@Preview
@Composable
private fun CategoryButtonFalsePreview() {
    YongProjectTheme {
        CategoryButton(
            title = "전체선택",
            isSelected = false,
        )
    }
}
