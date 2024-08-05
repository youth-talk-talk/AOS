package com.core.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.login.R
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun AgreeScreen(clickCancel: () -> Unit, clickNext: () -> Unit) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(top = 14.dp),
            text = "약관동의",
            style =
            MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.W700,
                color = Color.Black,
            ),
        )

        // 약관 동의 글
        DetailAgreeText()

        // 약관 동의 글 동의 여부 텍스트
        DetailAgreeTextQuestion()

        // 예 아니요 선택 버튼
        AgreeScreenButton(
            clickCancel = clickCancel,
            clickNext = clickNext,
        )
    }
}

@Composable
private fun AgreeScreenButton(clickCancel: () -> Unit, clickNext: () -> Unit) {
    Row(
        modifier =
        Modifier
            .padding(top = 14.dp, bottom = 21.dp)
            .padding(horizontal = 25.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RoundButton(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.no),
            color = Color(0xFFE3E5E5),
            onClick = clickCancel,
        )
        RoundButton(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.yes),
            color = MaterialTheme.colorScheme.primary,
            onClick = clickNext,
        )
    }
}

@Composable
private fun DetailAgreeTextQuestion() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.agree_subtitle),
            style =
            MaterialTheme.typography.bodyMedium
                .copy(fontWeight = FontWeight.W700),
        )
        Text(
            text = stringResource(id = R.string.agree_subtitle2),
            style =
            MaterialTheme.typography.bodySmall
                .copy(
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF929292),
                ),
        )
    }
}

@Composable
private fun ColumnScope.DetailAgreeText() {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 21.dp)
            .background(color = Color(0xFFf3f3f3))
            .weight(1f),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "약관 동의 글",
        )
    }
}

@Preview
@Composable
private fun AgreeScreenPreview() {
    YongProjectTheme {
        AgreeScreen(
            clickCancel = {},
            clickNext = {},
        )
    }
}
