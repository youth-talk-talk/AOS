package com.core.community.screen.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.community.R
import com.youthtalk.component.comment.UserComment
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommunityDetailScreen(modifier: Modifier = Modifier) {
    var textValue by remember {
        mutableStateOf("")
    }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        MiddleTitleTopBar(
            onBack = {},
            tails = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.share),
                        contentDescription = "공유하기",
                        colorFilter = ColorFilter.tint(color = gray100),
                    )

                    Image(
                        painter = painterResource(R.drawable.bookmark_line),
                        contentDescription = "공유하기",
                        colorFilter = ColorFilter.tint(color = gray100),
                    )

                    Image(
                        painter = painterResource(R.drawable.more),
                        contentDescription = "공유하기",
                        colorFilter = ColorFilter.tint(color = gray100),
                    )
                }
            },
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_thumnail),
                        contentDescription = "기본 이미지",
                    )

                    Column {
                        Text(
                            text = "씩씩한청년",
                            style = MaterialTheme.typography.displayLarge,
                        )
                        Text(
                            text = "3시간 전",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = gray80,
                            ),
                        )
                    }
                }
                HorizontalDivider(
                    color = gray40,
                )

                Box(
                    modifier = Modifier
                        .heightIn(min = 500.dp)
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 30.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("여기는 글 상세 부분 추후 작성 예정")
                }

                HorizontalDivider(
                    thickness = 10.dp,
                    color = gray30,
                )
            }

            items(
                count = 10,
            ) {
                if (it == 0) {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 20.dp,
                            top = 16.dp,
                        ),
                        text = "댓글 7",
                        style = MaterialTheme.typography.displayLarge,
                    )
                }

                UserComment(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(
                    color = gray30,
                    shape = RoundedCornerShape(6.dp),
                )
                .heightIn(max = 80.dp)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            scope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                value = textValue,
                onValueChange = { textValue = it },
                textStyle = MaterialTheme.typography.titleSmall,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Default,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {},
                ),
            ) { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    if (textValue.isEmpty()) {
                        Text(
                            text = "댓글을 입력해 보세요!",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = gray70,
                            ),
                        )
                    }
                    innerTextField()
                }
            }

            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.send),
                contentDescription = "보내기",
                colorFilter = ColorFilter.tint(color = if (textValue.isEmpty()) gray70 else gray90),
            )
        }
    }
}

@Preview
@Composable
private fun CommunityDetailPreview() {
    YongProjectTheme {
        CommunityDetailScreen()
    }
}
