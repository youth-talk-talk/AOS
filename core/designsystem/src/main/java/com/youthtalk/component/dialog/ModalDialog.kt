package com.youthtalk.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.youthtalk.component.button.Round6Button
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray50

@Composable
fun ModalDialog(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String = "",
    cancelText: String = "닫기",
    confirmText: String,
    confirmBackground: Color = MaterialTheme.colorScheme.primary,
    onDismissRequest: () -> Unit,
    onClickCancel: () -> Unit = {},
    onClickConfirm: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = gray10,
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                )

                if (subTitle.isNotEmpty()) {
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Round6Button(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = gray50,
                            shape = RoundedCornerShape(6.dp),
                        ),
                    text = cancelText,
                    onClick = {
                        onClickCancel()
                        onDismissRequest()
                    },
                )

                Round6Button(
                    modifier = Modifier
                        .weight(1f),
                    text = confirmText,
                    textColor = gray10,
                    backgroundColor = confirmBackground,
                    onClick = {
                        onClickConfirm()
                        onDismissRequest()
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ModalDialogPreview() {
    YongProjectTheme {
        ModalDialog(
            title = "로그아웃",
            subTitle = "로그아웃 하시겠습니까?",
            confirmText = "로그아웃",
            confirmBackground = MaterialTheme.colorScheme.error,
            onDismissRequest = {},
            onClickCancel = {},
            onClickConfirm = {},
        )
    }
}

@Preview
@Composable
private fun ModalDialogPrimaryPreview() {
    YongProjectTheme {
        ModalDialog(
            title = "프로필 편집 나가기",
            subTitle = "화면을 나가면 변경사항이 저장되지 않습니다.\n나가시겠습니까?",
            cancelText = "나가기",
            confirmText = "편집하기",
            onDismissRequest = {},
            onClickCancel = {},
            onClickConfirm = {},
        )
    }
}
