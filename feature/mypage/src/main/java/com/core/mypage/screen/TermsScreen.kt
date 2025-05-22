package com.core.mypage.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.mypage.R
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray90

@Composable
fun TermsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = stringResource(R.string.terms_topbar_title),
            onBack = {}
        )
        Column(
            modifier = Modifier
                .padding(vertical = 18.dp, horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.law_info),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = gray90
                )
            )
        }
    }
}
