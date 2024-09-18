package com.core.community.screen

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.core.community.model.CommunityWriteUiEffect
import com.core.community.model.CommunityWriteUiEvent
import com.core.community.model.CommunityWriteUiState
import com.core.community.model.ContentInfo
import com.core.community.viewmodel.CommunityWriteViewModel
import com.core.community.worker.UploadWork.uploadWorkManager
import com.youth.app.feature.community.R
import com.youthtalk.component.CustomDialog
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50
import com.youthtalk.model.SearchPolicy
import com.youthtalk.model.WriteInfo
import com.youthtalk.util.clickableSingle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.OutputStream
import java.util.UUID

@Composable
fun CommunityWriteScreen(
    type: String,
    viewModel: CommunityWriteViewModel = hiltViewModel(),
    onBack: () -> Unit,
    checkPermission: (String) -> Boolean,
    goDetail: (Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var showPictureDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }
    var requestPermission by remember { mutableStateOf("") }
    var repeatRequestDialog by remember { mutableStateOf(false) }
    var settingDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    BackHandler { deleteDialog = true }

    LaunchedEffect(key1 = viewModel.uieffect) {
        viewModel.uieffect.collectLatest {
            when (it) {
                is CommunityWriteUiEffect.GoDetail -> {
                    goDetail(it.id)
                }
            }
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { camera ->
        camera?.let {
            val uri = getImageUri(context, it)
            uploadWorkManager(
                context,
                scope,
                UUID.randomUUID(),
                uri,
                changeUri = { changeUri -> viewModel.uiEvent(CommunityWriteUiEvent.AddImage(changeUri)) },
            )
        }
    }

    val pictureLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadWorkManager(context, scope, UUID.randomUUID(), uri) { changeUri -> viewModel.uiEvent(CommunityWriteUiEvent.AddImage(changeUri)) }
        }
    }

    val cameraRequestPermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        } else {
            requestPermission = Manifest.permission.CAMERA
            if (checkPermission(requestPermission)) {
                repeatRequestDialog = true
            } else {
                settingDialog = true
            }
        }
    }

    val albumsRequestPermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            pictureLauncher.launch("image/*")
        } else {
            requestPermission = if (Build.VERSION.SDK_INT <= 32) Manifest.permission.READ_EXTERNAL_STORAGE else Manifest.permission.READ_MEDIA_IMAGES
            if (checkPermission(requestPermission)) {
                repeatRequestDialog = true
            } else {
                settingDialog = true
            }
        }
    }

    when (uiState) {
        is CommunityWriteUiState.Success -> {
            val state = uiState as CommunityWriteUiState.Success
            CommunityWrite(
                type,
                title = state.title,
                policies = state.searchPolicies.collectAsLazyPagingItems(),
                contents = state.contents,
                searchPolicy = state.selectPolicy,
                requestFocus = viewModel.focusRequest,
                onBack = { deleteDialog = true },
                onClickPicture = { showPictureDialog = true },
                onSearch = { viewModel.uiEvent(CommunityWriteUiEvent.SearchPolicies(it)) },
                onPolicyDialogClick = { viewModel.uiEvent(CommunityWriteUiEvent.SelectPolicy(it)) },
                onTitleTextChange = { viewModel.uiEvent(CommunityWriteUiEvent.ChangeTitleText(it)) },
                onChangeTextValue = { contentInfo, text -> viewModel.uiEvent(CommunityWriteUiEvent.ChangeContents(contentInfo, text)) },
                onDeleteText = { viewModel.uiEvent(CommunityWriteUiEvent.DeleteText) },
                onDeleteImage = { viewModel.uiEvent(CommunityWriteUiEvent.DeleteImage(it)) },
                createPost = { viewModel.uiEvent(CommunityWriteUiEvent.CreatePost(it)) },
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    if (showPictureDialog) {
        AddPictureDialog(
            onDismiss = { showPictureDialog = false },
            onCamera = {
                onRequestPermission(
                    context = context,
                    permission = Manifest.permission.CAMERA,
                    requestLauncher = { cameraRequestPermission.launch(it) },
                    success = {
                        cameraLauncher.launch()
                        showPictureDialog = false
                    },
                )
            },
            onAlbum = {
                onRequestPermission(
                    context = context,
                    permission = if (Build.VERSION.SDK_INT <= 32) {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    } else {
                        Manifest.permission.READ_MEDIA_IMAGES
                    },
                    requestLauncher = { albumsRequestPermission.launch(it) },
                    success = {
                        pictureLauncher.launch("image/*")
                        showPictureDialog = false
                    },
                )
            },
        )
    }

    if (deleteDialog) {
        CustomDialog(
            title = "글쓰기를 중단하시겠습니까?\n" +
                "작성중이던 글이 사라집니다",
            onCancel = { deleteDialog = false },
            onSuccess = { onBack() },
            onDismiss = { deleteDialog = false },
        )
    }

    if (repeatRequestDialog) {
        CustomDialog(
            title = "권한을 허용해야 기능을 사용할 수 있습니다.",
            cancelTitle = "나중에 요청하기",
            successTitle = "권한 요청하기",
            onCancel = { repeatRequestDialog = false },
            onSuccess = {
                if (requestPermission == Manifest.permission.CAMERA) {
                    cameraRequestPermission.launch(requestPermission)
                } else {
                    albumsRequestPermission.launch(requestPermission)
                }
            },
            onDismiss = { repeatRequestDialog = false },
        )
    }

    if (settingDialog) {
        CustomDialog(
            title = "설정 화면에서 권한을 허용해주세요",
            successTitle = "설정화면 이동",
            cancelTitle = "나중에 설정하기",
            onCancel = { settingDialog = false },
            onSuccess = {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            },
            onDismiss = { settingDialog = false },
        )
    }
}

private fun onRequestPermission(context: Context, permission: String, requestLauncher: (String) -> Unit, success: () -> Unit) {
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        // 성공했을 때
        success()
    } else {
        // 실패했을 때
        requestLauncher(permission)
    }
}

@Composable
private fun CommunityWrite(
    type: String,
    title: String,
    contents: ImmutableList<WriteInfo>,
    policies: LazyPagingItems<SearchPolicy>,
    searchPolicy: SearchPolicy?,
    requestFocus: SharedFlow<Int>,
    onClickPicture: () -> Unit,
    onBack: () -> Unit,
    onSearch: (String) -> Unit,
    onPolicyDialogClick: (SearchPolicy?) -> Unit,
    onTitleTextChange: (String) -> Unit,
    onChangeTextValue: (ContentInfo, String) -> Unit,
    onDeleteText: () -> Unit,
    onDeleteImage: (Int) -> Unit,
    createPost: (String) -> Unit,
) {
    val appbarTitle = if (type == "review") "후기글쓰기" else "자유글쓰기"
    var textFieldState by remember { mutableStateOf(TextFieldValue(text = title)) }
    var policyDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background,
            )
            .imePadding(),
    ) {
        MiddleTitleTopBar(title = appbarTitle, onBack = onBack)
        HorizontalDivider()
        TitleEditText(
            textFieldState,
            onTitleTextChange = {
                textFieldState = it
                if (it.text != title) {
                    onTitleTextChange(it.text)
                }
            },
        )
        if (type == "review") {
            PolicySearch(
                searchPolicy = searchPolicy,
                onClick = { policyDialog = true },
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        ContentEditText(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contents = contents,
            requestFocus = requestFocus,
            onClickPicture = onClickPicture,
            onChangeTextValue = onChangeTextValue,
            onDeleteText = onDeleteText,
            onDeleteImage = onDeleteImage,
        )
        RoundButton(
            modifier = Modifier
                .padding(horizontal = 17.dp)
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            text = "등록하기",
            enabled = checkEnabled(title, contents, type, searchPolicy),
            color = if (checkEnabled(title, contents, type, searchPolicy)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        ) {
            createPost(type)
        }
    }

    if (policyDialog) {
        PolicyDialog(
            policies = policies,
            onDismiss = { policyDialog = false },
            onSearch = onSearch,
            onClick = onPolicyDialogClick,
        )
    }
}

private fun checkEnabled(title: String, contents: ImmutableList<WriteInfo>, type: String, searchPolicy: SearchPolicy?): Boolean {
    return (
        title.isNotEmpty() && contents.any {
            it.uri != null || it.content.isNotEmpty()
        } && ((type == "review" && searchPolicy != null) || type == "post")
        )
}

@Composable
private fun PolicyDialog(
    policies: LazyPagingItems<SearchPolicy>,
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit,
    onClick: (SearchPolicy?) -> Unit,
) {
    var policyText by remember {
        mutableStateOf("")
    }
    var selectSearchPolicy by remember {
        mutableStateOf<SearchPolicy?>(null)
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        val focus = LocalFocusManager.current
        val ime = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(525.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(vertical = 20.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "정책검색",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondary,
                    ),
                )

                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    painter = painterResource(id = R.drawable.close_icon),
                    contentDescription = "닫기",
                    tint = Color.Black,
                )
            }

            BasicTextField(
                value = policyText,
                onValueChange = { policyText = it },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focus.clearFocus()
                        ime?.hide()
                        onSearch(policyText)
                    },
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                ),
            ) { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            shape = RoundedCornerShape(50.dp),
                        )
                        .padding(horizontal = 20.dp, vertical = 17.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (policyText.isEmpty()) {
                            Text(
                                text = "정책명을 검색해주세요",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = gray50,
                                ),
                            )
                        } else {
                            innerTextField()
                        }
                    }
                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            focus.clearFocus()
                            ime?.hide()
                            onSearch(policyText)
                        },
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "검색",
                        tint = Color.Black,
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                items(
                    count = policies.itemCount,
                    key = { index -> policies.peek(index)!!.policyId },
                ) { index ->
                    policies[index]?.let {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (it == selectSearchPolicy) {
                                        MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.5f,
                                        )
                                    } else {
                                        MaterialTheme.colorScheme.background
                                    },
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) {
                                    selectSearchPolicy = if (it == selectSearchPolicy) null else it
                                },
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 15.dp, top = 15.dp, bottom = 12.dp),
                                text = it.title,
                                style = MaterialTheme.typography.headlineLarge,
                            )
                            HorizontalDivider(modifier = Modifier.align(Alignment.BottomStart))
                        }
                    }
                }
            }

            RoundButton(
                modifier = Modifier.fillMaxWidth(),
                text = "추가하기",
                color = MaterialTheme.colorScheme.primary,
                onClick = {
                    onClick(selectSearchPolicy)
                    onDismiss()
                },
            )
        }
    }
}

@Composable
private fun AddPictureDialog(onDismiss: () -> Unit, onCamera: () -> Unit, onAlbum: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickableSingle { onDismiss() },
            verticalArrangement = Arrangement.Bottom,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(10.dp),
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 11.dp)
                        .onClickNoIndicator {
                            onAlbum()
                            onDismiss()
                        },
                    text = "앨범에서 선택",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                HorizontalDivider()
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 11.dp)
                        .onClickNoIndicator {
                            onCamera()
                            onDismiss()
                        },
                    text = "카메라로 이동",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.onClickNoIndicator { onDismiss() },
                    text = "취소",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun ContentEditText(
    modifier: Modifier = Modifier,
    contents: ImmutableList<WriteInfo>,
    requestFocus: SharedFlow<Int>,
    onClickPicture: () -> Unit,
    onChangeTextValue: (ContentInfo, String) -> Unit,
    onDeleteText: () -> Unit,
    onDeleteImage: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 17.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "내용작성",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        Box(
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(8.dp),
            ),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            ) {
                items(count = contents.size) { index ->
                    val content = contents[index]
                    var isFocus by remember { mutableStateOf(false) }
                    var textFieldValue by remember { mutableStateOf(TextFieldValue(content.content)) }
                    val focusRequest = remember { FocusRequester() }

                    LaunchedEffect(key1 = requestFocus) {
                        requestFocus.collectLatest {
                            if (index == it) focusRequest.requestFocus()
                        }
                    }
                    LaunchedEffect(content.content) {
                        textFieldValue = textFieldValue.copy(text = content.content)
                    }

                    content.uri?.let { uri ->
                        Box {
                            var isSuccess by remember {
                                mutableStateOf(false)
                            }
                            SubcomposeAsyncImage(
                                modifier = Modifier.padding(top = 16.dp, end = 16.dp),
                                model = uri,
                                contentDescription = null,
                                onSuccess = {
                                    isSuccess = true
                                },
                                loading = {
                                    CircularProgressIndicator()
                                },
                            )
                            if (isSuccess) {
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .onClickNoIndicator { onDeleteImage(index) },
                                    painter = painterResource(id = R.drawable.close_icon),
                                    contentDescription = "이미지 삭제 버튼",
                                    tint = Color.Black,
                                )
                            }
                        }
                    }

                    BasicTextField(
                        modifier = Modifier
                            .onFocusChanged { isFocus = it.isFocused }
                            .focusRequester(focusRequest)
                            .clearFocusOnKeyboardDismiss()
                            .onKeyEvent { event ->
                                when (event.key.keyCode) {
                                    287762808832 -> {
                                        onDeleteText()
                                    }

                                    else -> {}
                                }
                                true
                            },
                        value = textFieldValue,
                        onValueChange = {
                            textFieldValue = it
                            onChangeTextValue(ContentInfo(index, textFieldValue.selection.start), textFieldValue.text)
                        },
                    ) { innerTextField ->
                        if (textFieldValue.text.isEmpty() && !isFocus) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(MaterialTheme.colorScheme.background),
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background),
                            ) {
                                innerTextField()
                            }
                        }
                    }
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp, vertical = 12.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(vertical = 17.dp)
            .clickableSingle { onClickPicture() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.gallery_icon),
            contentDescription = "갤러리아이콘",
            tint = Color.Black,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "사진추가하기",
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Composable
private fun PolicySearch(searchPolicy: SearchPolicy?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 20.dp, start = 17.dp, end = 17.dp)
            .clickableSingle { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Text(
            modifier = Modifier.width(42.dp),
            text = "정책명",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 13.dp, vertical = 17.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = searchPolicy?.title ?: "정책명",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = searchPolicy?.let { Color.Black } ?: MaterialTheme.colorScheme.onTertiary,
                ),
            )

            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "검색",
                tint = Color.Black,
            )
        }
    }
}

@Composable
private fun TitleEditText(textFieldState: TextFieldValue, onTitleTextChange: (TextFieldValue) -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp, start = 17.dp, end = 17.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            modifier = Modifier.width(42.dp),
            text = "제목",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
        BasicTextField(
            textFieldState,
            onValueChange = onTitleTextChange,
            singleLine = true,
            modifier = Modifier
                .clearFocusOnKeyboardDismiss(),
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(horizontal = 13.dp, vertical = 17.dp),
            ) {
                if (textFieldState.text.isEmpty()) {
                    Text(
                        text = "제목을 작성해주세요",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
                innerTextField()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val imeIsVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}

@Composable
fun Modifier.onClickNoIndicator(click: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember {
            MutableInteractionSource()
        },
        indication = null,
        onClick = click,
    )
}

private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
    val contentResolver = context.contentResolver
    val fileName = "${System.currentTimeMillis()}.jpg"
    var fos: OutputStream?
    var imageUri: Uri?
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Video.Media.IS_PENDING, 1)
    }

    contentResolver.also { resolver ->
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }
    }

    fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    contentValues.clear()
    contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
    imageUri?.let { uri ->
        contentResolver.update(uri, contentValues, null, null)
    }

    return imageUri ?: Uri.EMPTY
}

@Preview
@Composable
private fun CommunityWriteScreenPreview() {
    YongProjectTheme {
        CommunityWriteScreen(
            type = "free",
            onBack = {},
            checkPermission = { false },
            goDetail = {},
        )
    }
}
