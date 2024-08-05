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
fun PolicyTitle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = "국토교통부",
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onTertiary,
            ),
        )

        Text(
            text = "청년 주택드림 청약통장",
            style = MaterialTheme.typography.bodyLarge,
        )

        Text(
            text = "청년의 내집 마련을 위해 장기, 저리 대출 지원",
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Preview
@Composable
private fun PolicyTitlePreview() {
    YongProjectTheme {
        PolicyTitle()
    }
}
