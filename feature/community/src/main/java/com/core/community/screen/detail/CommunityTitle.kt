package com.core.community.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun CommunityTitle(modifier: Modifier = Modifier, nickname: String, title: String, policyTitle: String?) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = nickname,
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )

        policyTitle?.let { policyTitle ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "정책명",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondary,
                    ),
                )
                Text(
                    text = policyTitle,
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = MaterialTheme.colorScheme.onTertiary,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommunityTitlePreview() {
    YongProjectTheme {
        CommunityTitle(nickname = "123", title = "123", policyTitle = "123")
    }
}
