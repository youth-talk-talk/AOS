package com.core.community.screen.write

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.core.community.model.CommunityWriteUiEffect
import com.core.community.model.CommunityWriteUiEvent
import com.core.community.model.CommunityWriteUiState
import com.core.community.viewmodel.CommunityWriteViewModel
import com.youthtalk.designsystem.YongProjectTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommunityWriteScreen(
    type: String,
    postId: Long,
    viewModel: CommunityWriteViewModel = hiltViewModel(),
    onBack: () -> Unit,
    checkPermission: (String) -> Boolean,
    goDetail: (Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPictureDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    BackHandler { deleteDialog = true }

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.uiEvent(CommunityWriteUiEvent.GetPostInfo(postId))
    }

    LaunchedEffect(key1 = viewModel.uieffect) {
        viewModel.uieffect.collectLatest {
            when (it) {
                is CommunityWriteUiEffect.GoDetail -> {
                    goDetail(it.id)
                }
            }
        }
    }

    when (uiState) {
        is CommunityWriteUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
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
                createPost = { viewModel.uiEvent(CommunityWriteUiEvent.CreatePost(it, postId)) },
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

    CommunityWriteDialog(
        context = context,
        showPictureDialog = showPictureDialog,
        onDismissShowPictureDialog = { showPictureDialog = false },
        checkPermission = checkPermission,
        addImage = { changeUri -> viewModel.uiEvent(CommunityWriteUiEvent.AddImage(changeUri)) },
        onBack = onBack,
        deleteDialog = deleteDialog,
        onDismissDeleteDialog = { deleteDialog = false },
    )
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

fun Modifier.onEmptyHeight(isEmpty: Boolean): Modifier = composed {
    if (isEmpty) {
        height(100.dp)
    } else {
        this
    }
}

@Preview
@Composable
private fun CommunityWriteScreenPreview() {
    YongProjectTheme {
        CommunityWriteScreen(
            type = "free",
            postId = -1,
            onBack = {},
            checkPermission = { false },
            goDetail = {},
        )
    }
}
