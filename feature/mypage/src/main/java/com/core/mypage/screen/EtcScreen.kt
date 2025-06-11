package com.core.mypage.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.mypage.model.etc.EtcUiEffect
import com.core.mypage.viewmodel.EtcViewModel
import com.youth.app.feature.mypage.R
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray90
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EtcScreen(viewModel: EtcViewModel = hiltViewModel(), onBack: () -> Unit, goLogin: () -> Unit, modifier: Modifier = Modifier) {
    var dialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collectLatest {
            when (it) {
                EtcUiEffect.Logout -> goLogin()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = stringResource(R.string.etc_topbar_title),
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 15.dp, end = 17.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.etc_app_version_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = gray90
                    )
                )

                Text(
                    text = "24.52.1 버전",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = gray90
                    )
                )
            }

            Text(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        dialog = true
                    }
                    .fillMaxWidth(),
                text = stringResource(R.string.withdraw),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = gray90
                )
            )
        }
    }

    if (dialog) {
        ModalDialog(
            title = stringResource(R.string.withdraw),
            subTitle = stringResource(R.string.dialog_withdraw_subtitle),
            confirmBackground = MaterialTheme.colorScheme.error,
            confirmText = stringResource(R.string.withdraw),
            onClickConfirm = { viewModel.postLogout(true) },
            onDismissRequest = { dialog = false }
        )
    }
}

@Preview
@Composable
private fun EtcScreenPreview() {
    YongProjectTheme {
        EtcScreen(
            onBack = {},
            goLogin = {}
        )
    }
}
