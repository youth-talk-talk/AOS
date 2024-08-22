package com.youthtalk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun SearchChip(
    modifier: Modifier = Modifier,
    shape: Shape,
    text: String,
    iconVisible: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    borderBackground: Color = MaterialTheme.colorScheme.primary,
) {
    Row(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = shape,
            )
            .border(
                width = 1.dp,
                shape = shape,
                color = borderBackground,
            )
            .padding(horizontal = 13.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (iconVisible) {
            Icon(
                painter = painterResource(id = R.drawable.close_icon),
                contentDescription = "삭제 아이콘",
                tint = Color.Black,
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Preview
@Composable
private fun RecentlyChipPreview() {
    YongProjectTheme {
        SearchChip(
            text = "월세지원",
            shape = RoundedCornerShape(43.dp),
        )
    }
}

@Preview
@Composable
private fun relativeChipPreview() {
    YongProjectTheme {
        SearchChip(
            text = "월세 대출",
            shape = RoundedCornerShape(50.dp),
            backgroundColor = MaterialTheme.colorScheme.onSecondaryContainer,
            borderBackground = MaterialTheme.colorScheme.onSecondaryContainer,
            iconVisible = false,
        )
    }
}
