package com.youthtalk.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray60

@Composable
fun RoundChip(modifier: Modifier = Modifier, text: String, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else gray10,
                shape = RoundedCornerShape(100.dp)
            )
            .isSelected(isSelected)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.displayMedium.copy(
                color = if (isSelected) gray10 else gray60
            )
        )
    }
}

private fun Modifier.isSelected(isSelected: Boolean): Modifier {
    return if (isSelected) {
        this
    } else {
        this.border(
            width = 1.dp,
            color = gray40,
            shape = RoundedCornerShape(100.dp)
        )
    }
}

@Preview
@Composable
private fun RoundChipSelectedPreview() {
    YongProjectTheme {
        RoundChip(
            text = "전체",
            isSelected = true
        )
    }
}

@Preview
@Composable
private fun RoundChipPreview() {
    YongProjectTheme {
        RoundChip(
            text = "주거"
        )
    }
}
