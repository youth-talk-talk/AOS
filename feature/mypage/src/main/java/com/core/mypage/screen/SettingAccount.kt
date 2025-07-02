package com.core.mypage.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.core.mypage.model.InfoType
import com.core.mypage.model.setting.SettingType
import com.core.mypage.model.setting.SettingUiEvent
import com.youth.app.feature.mypage.R
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.component.dropdown.RegionDropDown
import com.youthtalk.component.sheet.RegionBottomSheet
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Region
import com.youthtalk.model.typeenum.toRegionName

@Composable
fun SettingAccount(modifier: Modifier = Modifier, user: User, uploadLoading: Boolean, actionEvent: (SettingUiEvent) -> Unit) {
    val focusManager = LocalFocusManager.current
    var logoutDialog by remember {
        mutableStateOf(false)
    }
    var onBackDialog by remember {
        mutableStateOf(false)
    }

    var onSaveDialog by remember {
        mutableStateOf(false)
    }
    var bottomSheet by remember {
        mutableStateOf(false)
    }

    BackHandler {
        onBackDialog = true
    }

    Column(
        modifier = modifier
            .background(color = gray10)
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = stringResource(R.string.account_topbar_title),
            onBack = { onBackDialog = true },
            tails = {
                Text(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        focusManager.clearFocus()
                        onSaveDialog = true
                    },
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80
                    )
                )
            }
        )

        UserImage(
            profileImgUrl = user.profileImgUrl
        )

        UserInfo(
            nickname = user.nickname,
            region = user.region,
            onChangeValue = { value ->
                actionEvent(SettingUiEvent.OnChangeValue(value.trim()))
            },
            onClickRegion = { bottomSheet = true },
            onClickLogout = { logoutDialog = true }
        )
    }

    if (uploadLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = gray100.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (logoutDialog) {
        ModalDialog(
            title = stringResource(R.string.logout),
            subTitle = stringResource(R.string.dialog_logout_subtitle),
            confirmText = stringResource(R.string.logout),
            onDismissRequest = { logoutDialog = false },
            onClickConfirm = {
                actionEvent(SettingUiEvent.PostLogout(false))
            }
        )
    }

    if (onBackDialog) {
        ModalDialog(
            title = "프로필 편집 나가기",
            subTitle = "화면을 나가면 변경사항이 저장되지 않습니다.\n나가시겠습니까?",
            confirmText = "편집하기",
            cancelText = "나가기",
            onDismissRequest = { logoutDialog = false },
            onClickCancel = {
                actionEvent(SettingUiEvent.ChangeSettingType(SettingType.MAIN))
            }
        )
    }

    if (onSaveDialog) {
        ModalDialog(
            title = "프로필 저장",
            subTitle = "변경된 내용을 저장하시겠습니까?",
            confirmText = "저장하기",
            onDismissRequest = { onSaveDialog = false },
            onClickConfirm = {
                actionEvent(SettingUiEvent.OnSaveUser)
            }
        )
    }

    if (bottomSheet) {
        RegionBottomSheet(
            region = user.region,
            onDismiss = {
                bottomSheet = false
            },
            onClick = {
                actionEvent(SettingUiEvent.OnChangeRegion(it ?: Region.ALL))
            }
        )
    }
}

@Composable
private fun UserInfo(
    modifier: Modifier = Modifier,
    nickname: String,
    region: Region,
    onChangeValue: (String) -> Unit,
    onClickRegion: () -> Unit,
    onClickLogout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        InfoType.entries.forEach {
            val infoTitle = stringResource(
                when (it) {
                    InfoType.NICKNAME -> R.string.nickname
                    InfoType.REGION -> R.string.favorite_region
                }
            )
            AccountInfo(
                infoTitle = infoTitle,
                body = {
                    when (it) {
                        InfoType.NICKNAME -> {
                            BasicTextField(
                                value = nickname,
                                onValueChange = { value -> if (value.length < 8) onChangeValue(value.trim()) },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.displayMedium
                            ) { innerTextField ->
                                Box(
                                    modifier = Modifier.informationShape(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    innerTextField()
                                }
                            }
                        }

                        InfoType.REGION -> {
                            RegionDropDown(
                                hint = stringResource(R.string.account_region_hint),
                                select = region.toRegionName(),
                                onSelect = onClickRegion
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
                    onClickLogout()
                },
            text = stringResource(R.string.logout),
            style = MaterialTheme.typography.displayMedium.copy(
                color = gray70,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun UserImage(modifier: Modifier = Modifier, profileImgUrl: String?) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            profileImgUrl?.let { img ->
                AsyncImage(
                    modifier = Modifier
                        .size(94.dp)
                        .clip(CircleShape),
                    model = img,
                    contentDescription = "썸네일 이미지"
                )
            } ?: Image(
                modifier = Modifier
                    .size(94.dp)
                    .clip(CircleShape),
                painter = painterResource(com.youth.app.core.designsystem.R.drawable.profile_thumnail),
                contentDescription = "기본 이미지"
            )
        }
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
            user = User(
                memberId = 6757,
                nickname = "Alan Lane",
                profileImgUrl = null,
                region = Region.ALL
            ),
            actionEvent = {},
            uploadLoading = false
        )
    }
}
