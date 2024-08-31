package com.core.community.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.youth.app.feature.community.R
import com.youthtalk.component.CustomDialog
import com.youthtalk.component.MiddleTitleTopBar
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50
import com.youthtalk.util.clickableSingle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommunityWriteScreen(type: String, onBack: () -> Unit) {
    val appbarTitle = if (type == "review") "후기글쓰기" else "자유글쓰기"
    val textFieldState = rememberTextFieldState()
    var showPictureDialog by remember {
        mutableStateOf(false)
    }
    var policyDialog by remember {
        mutableStateOf(false)
    }
    var deleteDialog by remember {
        mutableStateOf(false)
    }

    BackHandler {
        deleteDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background,
            ),
    ) {
        MiddleTitleTopBar(title = appbarTitle, onBack = { deleteDialog = true })
        HorizontalDivider()
        TitleEditText(textFieldState)
        PolicySearch(
            onClick = { policyDialog = true },
        )
        Spacer(modifier = Modifier.height(12.dp))
        ContentEditText(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClickPicture = {
                showPictureDialog = true
            },
        )
        RoundButton(
            modifier = Modifier
                .padding(horizontal = 17.dp)
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            text = "등록하기",
            color = MaterialTheme.colorScheme.onSurface,
        ) {
        }
    }

    if (showPictureDialog) {
        AddPictureDialog(
            onDismiss = { showPictureDialog = false },
        )
    }

    if (policyDialog) {
        PolicyDialog(
            onDismiss = { policyDialog = false },
        )
    }

    if (deleteDialog) {
        CustomDialog(
            title = "글쓰기를 중단하시겠습니까?\n" +
                "작성중이던 글이 사라집니다",
            onCancel = {
                deleteDialog = false
            },
            onSuccess = {
                onBack()
            },
            onDismiss = { deleteDialog = false },
        )
    }
}

@Composable
private fun PolicyDialog(onDismiss: () -> Unit) {
    var policyText by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = { onDismiss() }) {
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
                    modifier = Modifier.align(Alignment.CenterEnd),
                    painter = painterResource(id = R.drawable.close_icon),
                    contentDescription = "닫기",
                    tint = Color.Black,
                )
            }

            BasicTextField(
                value = policyText,
                onValueChange = {
                    policyText = it
                },
                singleLine = true,
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
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "검색",
                        tint = Color.Black,
                    )
                }
            }

            // TODO: 추후 검색 화면 구현할 때 만들 예정
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black),
            )

            RoundButton(
                modifier = Modifier.fillMaxWidth(),
                text = "추가하기",
                color = MaterialTheme.colorScheme.primary,
            ) {
            }
        }
    }
}

@Composable
private fun AddPictureDialog(onDismiss: () -> Unit) {
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
                        .padding(vertical = 11.dp),
                    text = "앨범에서 선택",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                HorizontalDivider()
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 11.dp),
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
                    text = "취소",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun ContentEditText(modifier: Modifier = Modifier, onClickPicture: () -> Unit) {
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
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 16.dp, vertical = 18.dp),
        ) {
            Text(text = "글작성 부분")
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
private fun PolicySearch(onClick: () -> Unit) {
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
                text = "정책명",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
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
@OptIn(ExperimentalFoundationApi::class)
private fun TitleEditText(textFieldState: TextFieldState) {
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
        BasicTextField2(

            state = textFieldState,
            decorator = { innerTextField ->
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
            },
        )
    }
}

@Preview
@Composable
private fun CommunityWriteScreenPreview() {
    YongProjectTheme {
        CommunityWriteScreen(
            type = "free",
            onBack = {},
        )
    }
}
