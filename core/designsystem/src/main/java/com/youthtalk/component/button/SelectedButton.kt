package com.youthtalk.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray50

@Composable
fun SelectedButton(modifier: Modifier = Modifier, isSelected: Boolean = false, text: String, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else gray10,
                shape = RoundedCornerShape(6)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else gray50,
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
                color = gray100
            )
        )
    }
}

@Preview
@Composable
private fun SelectedButtonPreview() {
    YongProjectTheme {
        SelectedButton(
            text = "전체 지역",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SelectedButtonSelectedPreview() {
    YongProjectTheme {
        SelectedButton(
            isSelected = true,
            text = "전체 지역",
            onClick = {}
        )
    }
}
