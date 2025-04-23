package com.youthtalk.component.inputfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.error
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray60
import com.youthtalk.model.InputState

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    hint: String = "",
    disabledHint: String = "",
    text: String = "",
    enable: Boolean = true,
    onTextChange: (String) -> Unit,
    onCheckFilter: (String) -> Boolean = { false },
) {
    var inputState by remember {
        mutableStateOf(if (enable) InputState.DEFAULT else InputState.DISABLED)
    }

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .stateInput(inputState)
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .onFocusChanged { state ->
                inputState = if (state.hasFocus) {
                    InputState.PRESSED
                } else {
                    if (onCheckFilter(text)) {
                        InputState.ERROR
                    } else {
                        if (text.isEmpty()) {
                            InputState.DEFAULT
                        } else {
                            InputState.COMPLETE
                        }
                    }
                }
            },
        value = text,
        onValueChange = onTextChange,
        enabled = enable,
        textStyle = MaterialTheme.typography.titleSmall.copy(
            color = gray100,
        ),
    ) { innerTextField ->
        Box {
            if (text.isEmpty() || !enable) {
                Text(
                    if (enable) hint else disabledHint,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = gray60,
                    ),
                )
            } else {
                innerTextField()
            }
        }
    }
}

fun Modifier.stateInput(state: InputState): Modifier {
    val borderColor = when (state) {
        InputState.DEFAULT,
        InputState.COMPLETE,
        -> gray50

        InputState.PRESSED -> gray100
        InputState.DISABLED -> gray30
        InputState.ERROR -> error
    }

    val backgroundColor = when (state) {
        InputState.DISABLED -> gray30
        else -> gray10
    }

    return this
        .background(
            color = backgroundColor,
            shape = RoundedCornerShape(6.dp),
        )
        .border(
            width = 1.dp,
            color = borderColor,
            shape = RoundedCornerShape(6.dp),
        )
}

@Preview
@Composable
private fun InputTextFieldPreview() {
    YongProjectTheme {
        var text by remember {
            mutableStateOf("")
        }
        InputTextField(
            text = text,
            hint = "텍스트를 입력해 주세요.",
            onTextChange = { text = it },
        )
    }
}

@Preview
@Composable
private fun InputTextFieldDisabledPreview() {
    YongProjectTheme {
        var text by remember {
            mutableStateOf("")
        }
        InputTextField(
            text = text,
            hint = "텍스트를 입력해 주세요.",
            disabledHint = "텍스트를 입력할 수 없습니다.",
            onTextChange = { text = it },
            enable = false,
        )
    }
}
