package com.core.mypage.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.mypage.R
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray90

@Composable
fun EtcScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        MiddleTitleTopBar(
            title = stringResource(R.string.etc_topbar_title),
            onBack = {},
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 15.dp, end = 17.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.etc_app_version_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = gray90,
                    ),
                )

                Text(
                    text = "24.52.1 버전",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = gray90,
                    ),
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.withdraw),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = gray90,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun EtcScreenPreview() {
    YongProjectTheme {
        EtcScreen()
    }
}
