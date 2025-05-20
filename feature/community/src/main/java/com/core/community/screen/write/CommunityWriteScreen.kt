package com.core.community.screen.write

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.community.model.write.CommunityWriteUiEffect
import com.core.community.model.write.CommunityWriteUiEvent
import com.core.community.viewmodel.CommunityWriteViewModel
import com.core.navigation.CommunityWriteNavigation
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.component.picture.PictureScreen
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.post.PostSubject
import com.youthtalk.util.FileConverter
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun CommunityWriteScreen(
    modifier: Modifier = Modifier,
    viewModel: CommunityWriteViewModel = hiltViewModel(),
    checkPermission: (String) -> Boolean,
    onBack: () -> Unit,
    onCreate: (PostSubject) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val scrollState = rememberLazyListState()
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is CommunityWriteUiEffect.GoPictureScreen -> navController.navigate(CommunityWriteNavigation.Picture)
                is CommunityWriteUiEffect.OnBack -> navController.popBackStack()
                is CommunityWriteUiEffect.ScrollIndex -> {
                    scrollState.animateScrollToItem(it.index + 2)
                }

                is CommunityWriteUiEffect.CreatePost -> {
                    onCreate(state.postType)
                }
            }
        }
    }

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
            viewModel.setEvent(CommunityWriteUiEvent.GetImages)
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

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CommunityWriteNavigation.Write
    ) {
        composable<CommunityWriteNavigation.Write> {
            WriteScreen(
                state = state,
                scrollState = scrollState,
                onClickPolicySearch = { navController.navigate(CommunityWriteNavigation.PolicySearch) },
                onTextChangeValue = { index, text ->
                    viewModel.setEvent(CommunityWriteUiEvent.OnTextChangeValue(index, text))
                },
                checkPermission = {
                    if (permissionList.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }) {
                        viewModel.setEvent(CommunityWriteUiEvent.GetImages)
                    } else {
                        launcher.launch(permissionList)
                    }
                },
                onChangeFocus = { index, text ->
                    viewModel.setEvent(CommunityWriteUiEvent.FocusChange(index, text))
                },
                onTextTitleChangeValue = {
                    viewModel.setEvent(CommunityWriteUiEvent.ChangeTitle(it))
                },
                onBack = onBack,
                onPostCreatePost = { viewModel.setEvent(CommunityWriteUiEvent.PostCreatePost) }
            )
        }

        composable<CommunityWriteNavigation.PolicySearch> {
            PolicySearchScreen(
                searchPolicy = state.searchPolicy,
                searchPolicies = state.searchPolicies.collectAsLazyPagingItems(),
                onSearchPolicyChange = { viewModel.setEvent(CommunityWriteUiEvent.SearchPolicyChangeTextValue(it)) },
                onSearchPolicy = { viewModel.setEvent(CommunityWriteUiEvent.PostSearchPolicy(it)) },
                onBack = { viewModel.setEvent(CommunityWriteUiEvent.ClearSearchInfo) },
                onClickSearchPolicy = { viewModel.setEvent(CommunityWriteUiEvent.OnClickSearchPolicy(it)) }
            )
        }

        composable<CommunityWriteNavigation.Picture> {
            PictureScreen(
                images = state.images,
                onBack = { navController.popBackStack() },
                onSelectImage = { uri ->
                    FileConverter.uriToFile(context, uri)?.let {
                        viewModel.setEvent(CommunityWriteUiEvent.PostUploadImages(it))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun CommunityWriteFreeScreenPreview() {
    YongProjectTheme {
        CommunityWriteScreen(
            checkPermission = { true },
            onBack = {},
            onCreate = {}
        )
    }
}

@Preview
@Composable
private fun CommunityWriteReviewScreenPreview() {
    YongProjectTheme {
        CommunityWriteScreen(
            checkPermission = { true },
            onBack = {},
            onCreate = {}
        )
    }
}
