package com.core.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.login.R
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.util.clickableSingle

@Composable
fun AgreeScreen(clickCancel: () -> Unit, clickNext: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 17.dp, vertical = 13.dp),
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickableSingle { onBack() },
                painter = painterResource(com.youth.app.core.designsystem.R.drawable.left_icon),
                contentDescription = "뒤로가기",
            )

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "약관 동의",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

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
            MaterialTheme.typography.displayLarge,
        )
        Text(
            text = stringResource(id = R.string.agree_subtitle2),
            style =
            MaterialTheme.typography.displaySmall
                .copy(color = MaterialTheme.colorScheme.onSecondary),
        )
    }
}

@Composable
private fun ColumnScope.DetailAgreeText() {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 21.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .weight(1f)
            .verticalScroll(state = rememberScrollState()),
    ) {
        Text(
            text = stringResource(id = R.string.law_info),
            style = MaterialTheme.typography.displaySmall,
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
            onBack = {},
        )
    }
}
