package com.core.community.screen.write

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.core.community.model.Contents
import com.core.community.model.write.CommunityWriteUiState
import com.youth.app.feature.community.R
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.model.post.PostSubject
import timber.log.Timber

@Composable
fun WriteScreen(
    modifier: Modifier = Modifier,
    state: CommunityWriteUiState,
    scrollState: LazyListState,
    onClickPolicySearch: () -> Unit,
    onTextChangeValue: (Int, TextFieldValue) -> Unit,
    onTextTitleChangeValue: (String) -> Unit,
    onChangeFocus: (Int, TextFieldValue?) -> Unit,
    checkPermission: () -> Unit,
    onBack: () -> Unit,
    onPostCreatePost: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var dialog by remember {
        mutableStateOf(false)
    }
    BackHandler {
        dialog = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(gray10)
    ) {
        MiddleTitleTopBar(
            title = when (state.postType) {
                PostSubject.REVIEW -> "후기 글쓰기"
                PostSubject.POST -> "자유 글쓰기"
            },
            onBack = { dialog = true },
            tails = {
                Text(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (state.isValid) onPostCreatePost()
                    },
                    text = "등록",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (state.isValid) gray100 else gray70
                    )
                )
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = scrollState
        ) {
            item {
                if (state.postType == PostSubject.REVIEW) {
                    WritePolicySearch(
                        title = state.policyName,
                        onClickPolicySearch = onClickPolicySearch
                    )
                }
            }

            item {
                WriteTitleTextField(
                    title = state.title,
                    onTextChange = onTextTitleChangeValue
                )
            }

            state.contentList.forEachIndexed { index, content ->
                item {
                    when (content) {
                        is Contents.Text -> {
                            WriteContentText(
                                index = index,
                                size = state.contentList.size,
                                postType = state.postType,
                                content = content.textFieldValue,
                                onTextChangeValue = onTextChangeValue
                            )
                        }

                        is Contents.Image -> {
                            WriteContentImage(
                                imgUrl = content.imgUrl,
                                onChangeFocus = { onChangeFocus(index, it) }
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
        ) {
            HorizontalDivider(
                color = gray40
            )

            Image(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        focusManager.clearFocus()
                        checkPermission()
                    },
                painter = painterResource(R.drawable.add_picture),
                contentDescription = "사진 추가"
            )
        }
    }

    if (state.uploadLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = gray100.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (dialog) {
        ModalDialog(
            title = "글쓰기를 중단할까요?",
            subTitle = "글쓰기를 중단하면 작성중이던 글이 모두 사라집니다.",
            confirmText = "작성하기",
            confirmBackground = MaterialTheme.colorScheme.error,
            onClickConfirm = { },
            onClickCancel = onBack,
            onDismissRequest = { dialog = false }
        )
    }
}

@Composable
fun WriteContentImage(modifier: Modifier = Modifier, imgUrl: String, onChangeFocus: (TextFieldValue?) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    var isFocus by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .onFocusChanged {
                if (it.hasFocus) {
                    onChangeFocus(null)
                }
                isFocus = it.hasFocus
            }
            .focusRequester(focusRequester)
            .focusable()
            .hasFocusBorder(isFocus)
            .fillMaxWidth()
            .heightIn(max = 400.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusRequester.requestFocus()
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = imgUrl,
            contentDescription = null
        )

        if (isFocus) {
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp),
                painter = painterResource(R.drawable.close),
                contentDescription = null
            )
        }
    }
}

@Composable
fun WritePolicySearch(modifier: Modifier = Modifier, title: String?, onClickPolicySearch: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClickPolicySearch()
            }
            .padding(horizontal = 16.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title ?: "정책 선택",
            style = MaterialTheme.typography.titleSmall
        )

        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.search),
            contentDescription = "검색",
            colorFilter = ColorFilter.tint(color = gray100)
        )
    }
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = gray40
    )
}

@Composable
fun WriteTitleTextField(modifier: Modifier = Modifier, title: String, onTextChange: (String) -> Unit) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = title,
        onValueChange = onTextChange,
        textStyle = MaterialTheme.typography.titleMedium,
        maxLines = 1
    ) { innerTextField ->
        Box {
            if (title.isEmpty()) {
                Text(
                    text = "제목을 입력해주세요.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = gray80
                    )
                )
            }
            innerTextField()
        }
    }
    HorizontalDivider(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        color = gray40
    )
}

@Composable
fun WriteContentText(
    modifier: Modifier = Modifier,
    index: Int,
    size: Int,
    content: TextFieldValue,
    postType: PostSubject,
    onTextChangeValue: (Int, TextFieldValue) -> Unit
) {
    var textFocus by remember { mutableStateOf(false) }

    BasicTextField(
        modifier = modifier
            .onKeyEvent {
                if (it.key == Key.Backspace && index != 0) {
                    // 여기가 백스페이스 누를 때 처리
                }
                false
            }
            .onFocusChanged {
                textFocus = it.hasFocus
            }
            .then(
                when {
                    index == size - 1 && index != 0 -> Modifier.height(150.dp)
                    !textFocus && content.text.isEmpty() && size != 1 -> Modifier.height(
                        3.dp
                    )

                    else -> Modifier.wrapContentHeight()
                }
            ),
        value = content,
        onValueChange = { textField ->
            Timber.e("start cursor index -> ${textField.selection.start}, value : ${textField.text}")
            onTextChangeValue(index, textField)
        },
        textStyle = MaterialTheme.typography.titleMedium
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (content.text.isEmpty() && size == 1) {
                Text(
                    text = when (postType) {
                        PostSubject.REVIEW -> stringResource(R.string.review_content_hint)
                        PostSubject.POST -> stringResource(R.string.free_content_hint)
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80
                    )
                )
            }
            innerTextField()
        }
    }
}

private fun Modifier.hasFocusBorder(isFocus: Boolean): Modifier {
    return if (isFocus) {
        this.border(
            width = 1.dp,
            color = gray100
        )
    } else {
        this
    }
}
