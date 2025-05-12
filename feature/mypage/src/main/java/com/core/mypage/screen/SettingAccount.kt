package com.core.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.mypage.model.InfoType
import com.core.mypage.model.account.AccountUiEffect
import com.core.mypage.viewmodel.AccountViewModel
import com.youth.app.feature.mypage.R
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.component.dropdown.RegionDropDown
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun SettingAccount(modifier: Modifier = Modifier, viewModel: AccountViewModel = hiltViewModel(), goLogin: () -> Unit) {
    var dialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collectLatest {
            when (it) {
                AccountUiEffect.Logout -> goLogin()
            }
        }
    }
    Column(
        modifier = modifier
            .background(color = gray10)
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = stringResource(R.string.account_topbar_title),
            onBack = {},
            tails = {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80
                    )
                )
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                // TODO: 이미지로 바꾸기
                Box(
                    modifier = Modifier
                        .size(94.dp)
                        .clip(CircleShape)
                        .background(gray50)
                        .align(Alignment.Center)
                )

                Image(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    painter = painterResource(R.drawable.camera),
                    contentDescription = stringResource(R.string.camera)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            InfoType.entries.forEach {
                val infoTitle = stringResource(
                    when (it) {
                        InfoType.NICKNAME -> R.string.nickname
                        InfoType.ACCOUNT -> R.string.account
                        InfoType.REGION -> R.string.favorite_region
                    }
                )
                AccountInfo(
                    infoTitle = infoTitle,
                    body = {
                        when (it) {
                            InfoType.NICKNAME -> {
                                Row(
                                    modifier = modifier
                                        .informationShape(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "울적한 쿠키(닉네임)",
                                        style = MaterialTheme.typography.displayMedium
                                    )
                                }
                            }

                            InfoType.ACCOUNT -> {
                                Row(
                                    modifier = modifier
                                        .informationShape(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier.padding(end = 8.dp),
                                        painter = painterResource(R.drawable.kakao),
                                        contentDescription = stringResource(R.string.kakao)
                                    )

                                    Text(
                                        text = "abcd@kakao.com",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }

                            InfoType.REGION -> {
                                RegionDropDown(
                                    hint = stringResource(R.string.account_region_hint),
                                    onSelect = {}
                                )
                            }
                        }
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp),
                thickness = 1.dp,
                color = gray40
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        dialog = true
                    },
                text = stringResource(R.string.logout),
                style = MaterialTheme.typography.displayMedium.copy(
                    color = gray70,
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    if (dialog) {
        ModalDialog(
            title = stringResource(R.string.logout),
            subTitle = stringResource(R.string.dialog_logout_subtitle),
            confirmText = stringResource(R.string.logout),
            onDismissRequest = { dialog = false },
            onClickConfirm = {
                Timber.e("dialog Clicked")
                viewModel.postLogout(false)
            }
        )
    }
}

private fun Modifier.informationShape() = this
    .fillMaxWidth()
    .heightIn(min = 46.dp)
    .border(
        width = 1.dp,
        color = gray50,
        shape = RoundedCornerShape(6.dp)
    )
    .padding(horizontal = 12.dp, vertical = 10.dp)

@Composable
fun AccountInfo(modifier: Modifier = Modifier, infoTitle: String, body: @Composable () -> Unit) {
    Text(
        modifier = modifier.padding(top = 20.dp, bottom = 12.dp),
        text = infoTitle,
        style = MaterialTheme.typography.titleSmall.copy(
            color = gray90
        )
    )

    body()
}

@Preview
@Composable
private fun SettingAccountPreview() {
    YongProjectTheme {
        SettingAccount(
            goLogin = {}
        )
    }
}
