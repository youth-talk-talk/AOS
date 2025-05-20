package com.core.mypage.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.mypage.component.ProfileCard
import com.core.mypage.model.CommunityModel
import com.core.mypage.model.ManageModel
import com.core.mypage.model.SettingModel
import com.core.mypage.model.setting.SettingType
import com.core.mypage.model.setting.SettingUiEffect
import com.core.mypage.model.setting.SettingUiEvent
import com.core.mypage.viewmodel.SettingViewModel
import com.core.navigation.model.CommentType
import com.core.navigation.model.ScrapPostType
import com.youth.app.feature.mypage.R
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.component.picture.PictureScreen
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Region
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onClickEtc: () -> Unit,
    onClickTerms: () -> Unit,
    onClickSettingScrap: () -> Unit,
    onClickSettingPost: (ScrapPostType) -> Unit,
    onClickSettingComment: (CommentType) -> Unit,
    onClickSettingNotification: () -> Unit,
    goLogin: () -> Unit,
    checkPermission: (String) -> Boolean
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val permissionState = viewModel.permissions
    val permissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { (_, isGranted) -> isGranted }) {
            viewModel.setEvent(SettingUiEvent.ChangeSettingType(SettingType.IMAGE))
        } else {
            permissions.forEach { (permission, isGranted) ->
                Timber.e("permission $permission, isGranted $isGranted")
                viewModel.onPermissionResult(
                    permission,
                    isGranted
                )
            }
        }
    }

    if (permissionState.isNotEmpty()) {
        if (permissionState.any { !checkPermission(it) }) {
            ModalDialog(
                title = "설정 화면으로 이동",
                subTitle = "설정화면에서 권한을 변경해 주세요.",
                cancelText = "나중에",
                confirmText = "설정 화면 이동",
                onDismissRequest = { viewModel.dismissDialog() },
                onClickConfirm = {
                    viewModel.dismissDialog()
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                    )
                }
            )
        } else {
            ModalDialog(
                title = "권한 허용",
                subTitle = "모든 권한을 허용해야 기능을 이용할 수 있습니다.",
                cancelText = "",
                confirmText = "설정하기",
                onDismissRequest = { viewModel.dismissDialog() },
                onClickConfirm = {
                    viewModel.dismissDialog()
                    launcher.launch(permissionList)
                }
            )
        }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                SettingUiEffect.Logout -> goLogin()
            }
        }
    }

    Crossfade(
        targetState = state.settingInfoType
    ) {
        when (it) {
            SettingType.MAIN -> {
                SettingMain(
                    user = state.user,
                    onClickEtc = onClickEtc,
                    onClickTerms = onClickTerms,
                    onClickSettingScrap = onClickSettingScrap,
                    onClickSettingPost = onClickSettingPost,
                    onClickSettingComment = onClickSettingComment,
                    onClickSettingNotification = onClickSettingNotification,
                    actionEvent = viewModel::setEvent
                )
            }

            SettingType.ACCOUNT -> SettingAccount(
                user = state.accountUser,
                uploadLoading = state.uploadLoading,
                actionEvent = viewModel::setEvent,
                checkPermission = {
                    if (permissionList
                            .all { permission -> ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED }
                    ) {
                        viewModel.setEvent(SettingUiEvent.ChangeSettingType(SettingType.IMAGE))
                    } else {
                        launcher.launch(permissionList)
                    }
                }
            )

            SettingType.IMAGE -> PictureScreen(
                images = state.images,
                onBack = { viewModel.setEvent(SettingUiEvent.ChangeSettingType(SettingType.ACCOUNT)) },
                onSelectImage = { file -> viewModel.setEvent(SettingUiEvent.PostUploadImages(file)) }
            )
        }
    }
}

@Composable
fun SettingMain(
    modifier: Modifier = Modifier,
    user: User,
    actionEvent: (SettingUiEvent) -> Unit,
    onClickEtc: () -> Unit,
    onClickTerms: () -> Unit,
    onClickSettingScrap: () -> Unit,
    onClickSettingPost: (ScrapPostType) -> Unit,
    onClickSettingComment: (CommentType) -> Unit,
    onClickSettingNotification: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        NoBackMiddleTitleTopBar(
            title = stringResource(R.string.appbar)
        )
        ProfileCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            user = user,
            onClick = { actionEvent(SettingUiEvent.ChangeSettingType(SettingType.ACCOUNT)) }
        )

        ScrapPolicyAndNotification(
            onClickSettingScrap = onClickSettingScrap,
            onClickSettingNotification = onClickSettingNotification
        )

        HorizontalDivider(
            thickness = 10.dp,
            color = gray30
        )

        SettingList<CommunityModel>(
            modifier = Modifier
                .padding(top = 30.dp, start = 15.dp, end = 17.dp, bottom = 20.dp),
            title = stringResource(R.string.community),
            contents = CommunityModel.getList(),
            onClick = {
                when (it) {
                    is CommunityModel.Comment -> {
                        onClickSettingComment(CommentType.MY)
                    }

                    is CommunityModel.Like -> {
                        onClickSettingComment(CommentType.LIKE)
                    }

                    is CommunityModel.Scrap -> {
                        onClickSettingPost(ScrapPostType.SCRAP)
                    }

                    is CommunityModel.Write -> {
                        onClickSettingPost(ScrapPostType.MY)
                    }
                }
            }
        )

        HorizontalDivider(
            thickness = 10.dp,
            color = gray30
        )

        SettingList<ManageModel>(
            modifier = Modifier
                .padding(top = 20.dp, start = 15.dp, end = 17.dp, bottom = 8.dp),
            title = stringResource(R.string.manage),
            contents = ManageModel.getList(),
            onClick = {
                when (it) {
                    is ManageModel.Policy -> onClickTerms()
                    is ManageModel.Inquire -> {}
                    is ManageModel.Etc -> onClickEtc()
                }
            }
        )
    }
}

@Composable
private fun <T : SettingModel> SettingList(modifier: Modifier = Modifier, title: String, contents: List<T>, onClick: (T) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            contents.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onClick(it)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = it.getSettingName(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = gray90
                        )
                    )

                    Image(
                        painter = painterResource(R.drawable.arrowright),
                        contentDescription = stringResource(R.string.next)
                    )
                }
            }
        }
    }
}

@Composable
fun ScrapPolicyAndNotification(modifier: Modifier = Modifier, onClickSettingScrap: () -> Unit, onClickSettingNotification: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClickSettingScrap()
                }
                .padding(top = 16.dp, bottom = 24.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.bookmark_line),
                contentDescription = stringResource(R.string.scrap_title)
            )
            Text(
                text = stringResource(R.string.scrap_title),
                style = MaterialTheme.typography.displayMedium
            )
        }

        VerticalDivider(
            modifier = Modifier.height(52.dp),
            thickness = 1.dp,
            color = gray40
        )

        Column(
            modifier = modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClickSettingNotification()
                }
                .padding(top = 16.dp, bottom = 24.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.notification),
                contentDescription = stringResource(R.string.notification)
            )
            Text(
                text = stringResource(R.string.notification),
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    YongProjectTheme {
        SettingMain(
            user = User(
                memberId = 7661,
                nickname = "Tyrone Martin",
                profileImgUrl = null,
                region = Region.ALL
            ),
            actionEvent = {},
            onClickEtc = {},
            onClickTerms = {},
            onClickSettingScrap = {},
            onClickSettingPost = {},
            onClickSettingComment = {},
            onClickSettingNotification = {}
        )
    }
}
