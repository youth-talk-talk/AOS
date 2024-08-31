package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.mypage.component.AnnouncementComponent
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun AnnouncementScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background,
            ),
    ) {
        MiddleTitleTopBar(
            title = "공지사항",
            onBack = onBack,
        )
        HorizontalDivider()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 17.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            items(
                count = 50,
            ) { _ ->
                AnnouncementComponent()
            }
        }
    }
}

@Preview
@Composable
private fun AnnouncementScreenPreview() {
    YongProjectTheme {
        AnnouncementScreen(
            onBack = {},
        )
    }
}
