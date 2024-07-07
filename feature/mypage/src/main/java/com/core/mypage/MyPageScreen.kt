package com.core.mypage

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun MyPageScreen() {
    Text(text = "MyPageScreen")
}

@Preview
@Composable
private fun MyPagePreview() {
    YongProjectTheme {
        MyPageScreen()
    }
}
