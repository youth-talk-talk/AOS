package com.youthtalk.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import java.time.LocalDate

@Composable
fun DayChip(modifier: Modifier = Modifier, isSelected: Boolean = false, dayOfWeek: String = "오늘", day: Int, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .widthIn(min = 40.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else gray10,
                shape = if (isSelected) RoundedCornerShape(20.dp) else RectangleShape,
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onClick()
            }
            .padding(horizontal = 9.dp, vertical = 11.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.labelMedium.copy(
                color = if (isSelected) gray10 else gray80,
            ),
        )

        Text(
            text = "$day",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isSelected) gray10 else gray70,
            ),
        )
    }
}

@Preview
@Composable
private fun DayChipPreview() {
    YongProjectTheme {
        DayChip(
            day = LocalDate.now().dayOfMonth,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun DayChipIsSelectedPreview() {
    YongProjectTheme {
        DayChip(
            isSelected = true,
            day = LocalDate.now().plusDays(10).dayOfMonth,
            onClick = {},
        )
    }
}
