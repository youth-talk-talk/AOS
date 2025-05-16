package com.core.community.screen.write

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.core.community.model.Contents
import com.core.community.model.write.CommunityWriteUiState
import com.youth.app.feature.community.R
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
    onClickPolicySearch: () -> Unit,
    onTextChangeValue: (Int, TextFieldValue) -> Unit,
    checkPermission: () -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(gray10)
    ) {
        MiddleTitleTopBar(
            title = when (state.postType) {
                PostSubject.REVIEW -> "후기 글쓰기"
                PostSubject.FREE -> "자유 글쓰기"
            },
            onBack = {},
            tails = {
                Text(
                    text = "등록",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray70
                    )
                )
            }
        )

        if (state.postType == PostSubject.REVIEW) {
            Row(
                modifier = Modifier
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
                    text = "정책 선택",
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

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = title,
            onValueChange = { title = it },
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
            modifier = Modifier.padding(horizontal = 16.dp),
            color = gray40
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .weight(1f)
        ) {
            state.contentList.forEachIndexed { index, content ->
                when (content) {
                    is Contents.Text -> {
                        BasicTextField(
                            modifier = Modifier
                                .onKeyEvent {
                                    if (it.key == Key.Backspace && index != 0) {
                                        // 여기가 백스페이스 누를 때 처리
                                    }
                                    false
                                },
                            value = content.textFieldValue,
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
                                if (content.textFieldValue.text.isEmpty() && state.contentList.size == 1) {
                                    Text(
                                        text = when (state.postType) {
                                            PostSubject.REVIEW -> stringResource(R.string.review_content_hint)
                                            PostSubject.FREE -> stringResource(R.string.free_content_hint)
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

                    is Contents.Image -> {
                    }
                }
            }
        }

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
                    checkPermission()
                },
            painter = painterResource(R.drawable.add_picture),
            contentDescription = "사진 추가"
        )
    }
}
