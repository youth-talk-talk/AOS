package com.core.community.screen.write

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.core.community.model.ContentInfo
import com.youth.app.feature.community.R
import com.youthtalk.model.WriteInfo
import com.youthtalk.util.clickableSingle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentEditText(
    modifier: Modifier = Modifier,
    type: String,
    contents: ImmutableList<WriteInfo>,
    requestFocus: SharedFlow<Int>,
    onClickPicture: () -> Unit,
    onChangeTextValue: (ContentInfo, String) -> Unit,
    onDeleteText: () -> Unit,
    onDeleteImage: (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
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
                    .fillMaxWidth()
                    .height(600.dp)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            ) {
                items(count = contents.size) { index ->
                    val content = contents[index]
                    var isFocus by remember { mutableStateOf(false) }
                    var textFieldValue by remember { mutableStateOf(TextFieldValue(content.content ?: "")) }
                    val focusRequest = remember { FocusRequester() }
                    val bringIntoViewRequester = remember { BringIntoViewRequester() }
                    var height by remember { mutableIntStateOf(-1) }
                    LaunchedEffect(key1 = requestFocus) {
                        requestFocus.collect {
                            if (index == it) focusRequest.requestFocus()
                        }
                    }
                    LaunchedEffect(content.content) {
                        textFieldValue = textFieldValue.copy(text = content.content ?: "")
                    }

                    content.uri?.let { uri ->
                        Box {
                            var isSuccess by remember {
                                mutableStateOf(false)
                            }
                            SubcomposeAsyncImage(
                                model = uri,
                                contentDescription = null,
                                onSuccess = {
                                    isSuccess = true
                                    height = it.painter.intrinsicSize.height.toInt()
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
                            .bringIntoViewRequester(bringIntoViewRequester)
                            .clearFocusOnKeyboardDismiss()
                            .onKeyEvent { event ->
                                when (event.key.keyCode) {
                                    287762808832 -> {
                                        if (textFieldValue.text.isEmpty()) {
                                            onDeleteText()
                                        }
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
                        onTextLayout = {
                            val cursorRect = it.getCursorRect(textFieldValue.selection.start) // Get Text Field cursor position
                            scope.launch {
                                bringIntoViewRequester.bringIntoView(cursorRect) // Scroll to Text Field cursor position by typing
                            }
                        },
                    ) { innerTextField ->
                        if (index != 0 && index != contents.size - 1) {
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
                        } else {
                            if (index == 0) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onEmptyHeight(contents.size == 1 && contents[index].content.isNullOrEmpty())
                                        .background(MaterialTheme.colorScheme.background),
                                ) {
                                    if (contents.size == 1 && contents[index].content.isNullOrEmpty()) {
                                        if (type == "review") {
                                            Text(
                                                text = stringResource(id = R.string.review_content_hint),
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    color = MaterialTheme.colorScheme.onTertiary,
                                                ),
                                            )
                                        }
                                    }
                                    innerTextField()
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
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
