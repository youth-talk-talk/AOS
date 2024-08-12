package com.example.policydetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun PolicyTitle(modifier: Modifier = Modifier, topTitle: String, mainTitle: String, titleDescription: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = topTitle,
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onTertiary,
            ),
        )

        Text(
            text = mainTitle,
            style = MaterialTheme.typography.bodyLarge,
        )

        Text(
            text = titleDescription,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Preview
@Composable
private fun PolicyTitlePreview() {
    YongProjectTheme {
        PolicyTitle(
            topTitle = "국토교통부",
            mainTitle = "청년 주택드림 청약통장",
            titleDescription = "청년의 내집 마련을 위해 장기, 저리 대출 지원",
        )
    }
}
