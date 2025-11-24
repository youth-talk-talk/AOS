package com.core.screen

import androidx.compose.runtime.Composable
import com.youthtalk.component.dialog.ModalDialog

@Composable
internal fun SystemEndScreen(onClickFinish: () -> Unit) {
    ModalDialog(
        title = "내부 사정으로 인해 더 이상 청년톡톡 지원이 불가합니다.",
        subTitle = "그 동안 청년톡톡을 사랑해주셔서 감사합니다.",
        cancelText = "",
        confirmText = "종료하기",
        onDismissRequest = {},
        onClickConfirm = {
            onClickFinish()
        }
    )
}
