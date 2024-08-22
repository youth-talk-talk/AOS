package com.core.community.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun CommunityWriteScreen() {
    Text(text = "글쓰기 화면")
}

@Preview
@Composable
private fun CommunityWriteScreenPreview() {
    YongProjectTheme {
        CommunityWriteScreen()
    }
}
