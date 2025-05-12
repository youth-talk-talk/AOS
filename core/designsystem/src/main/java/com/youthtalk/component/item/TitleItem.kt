package com.youthtalk.component.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10

@Composable
fun TitleItem(modifier: Modifier = Modifier, title: String, isVisibleArrow: Boolean = true, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = gray10)
            .padding(horizontal = 16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        if (isVisibleArrow) {
            Image(
                painter = painterResource(R.drawable.arrowright),
                contentDescription = "오른쪽 화살표"
            )
        }
    }
}

@Preview
@Composable
fun TitleItemPreview(modifier: Modifier = Modifier) {
    YongProjectTheme {
        TitleItem(
            title = "우리 지역 인기 정책",
            onClick = {}
        )
    }
}
