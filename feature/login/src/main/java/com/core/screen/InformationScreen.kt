package com.core.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.component.DropDownComponent
import com.core.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun InformationScreen() {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InformationTitleScreen()

        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 58.dp)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            NickNameScreen()
            LocationSelectScreen()
        }

        RoundButton(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 76.dp)
                .padding(horizontal = 24.dp),
            text = "시작하기",
            color = Color(0xFFE3E5E5),
        )
    }
}

@Composable
private fun LocationSelectScreen() {
    Text(
        modifier = Modifier.padding(start = 28.dp),
        text = "지역 설정",
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W600),
    )

    DropDownComponent(
        modifier = Modifier.padding(horizontal = 18.dp),
        dropDownClick = { /*TODO*/ },
    )
}

@Composable
private fun NickNameScreen() {
    Text(
        modifier = Modifier.padding(start = 28.dp),
        text = "닉네임 설정",
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W600),
    )
    BasicTextField(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp)
            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = Color(0xFFCDCFD0)),
        value = "123",
        textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W700),
        onValueChange = {},
        singleLine = true,
    ) { innerTextField ->

        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, top = 10.dp, bottom = 10.dp),
        ) {
            innerTextField()
        }
    }
    Text(
        modifier = Modifier.padding(start = 25.dp),
        text = "원하는 닉네임이 있는 경우 직접 설정 가능해요!(단, 한글8자 이내)",
        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFA5A5A5)),
    )
}

@Composable
private fun InformationTitleScreen() {
    Text(
        modifier =
        Modifier
            .padding(top = 35.dp),
        text = "청년톡톡과 함께할 정보를 입력해주세요",
        style =
        MaterialTheme.typography.displayLarge
            .copy(fontWeight = FontWeight.W700),
    )
}

@Preview
@Composable
private fun InformationScreenPreview() {
    YongProjectTheme {
        InformationScreen()
    }
}
