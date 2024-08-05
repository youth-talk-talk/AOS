package com.youthtalk.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun RoundButton(modifier: Modifier = Modifier, text: String, color: Color, enabled: Boolean = true, onClick: () -> Unit) {
    val mutableInteractionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier =
        modifier
            .background(color, shape = RoundedCornerShape(48.dp))
            .padding(vertical = 16.dp)
            .clickable(
                interactionSource = mutableInteractionSource,
                indication = null,
            ) {
                if (enabled) onClick()
            },
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
private fun RoundButtonNoPreview() {
    YongProjectTheme(darkTheme = false) {
        RoundButton(
            text = "아니요",
            onClick = {},
            color = Color(0xFFE3E5E5),
            enabled = false,
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
            enabled = true,
            onClick = {},
        )
    }
}
