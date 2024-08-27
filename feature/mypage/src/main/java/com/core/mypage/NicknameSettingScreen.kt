package com.core.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.mypage.component.account.AccountTopBar
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray

@Composable
fun NicknameSettingScreen(modifier: Modifier = Modifier, nickname: String, onBack: () -> Unit) {
    var name by remember {
        mutableStateOf(nickname)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        AccountTopBar(
            title = "닉네임 설정",
            onBack = onBack,
        )
        HorizontalDivider()

        Text(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 17.dp),
            text = "닉네임 설정",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp),
            value = name,
            onValueChange = {
                name = it
            },
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = gray,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(13.dp),
            ) {
                innerTextField()
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        RoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp, vertical = 12.dp),
            text = "적용하기",
            color = MaterialTheme.colorScheme.primary,
        ) {
        }
    }
}

@Preview
@Composable
private fun NicknameSettingScreenPreview() {
    YongProjectTheme {
        NicknameSettingScreen(
            nickname = "놀고픈 청년",
            onBack = {},
        )
    }
}
