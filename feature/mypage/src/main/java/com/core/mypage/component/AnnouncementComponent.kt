package com.core.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun AnnouncementComponent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(4.dp),
                clip = false,
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(13.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "2024/08/27",
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        Text(
            text = "공지사항_제목 공지사항_제목 칸입니다.",
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
private fun AnnouncementComponentPreview() {
    YongProjectTheme {
        AnnouncementComponent()
    }
}
