package com.core.community.model

import androidx.compose.ui.text.input.TextFieldValue

sealed interface Contents {
    data class Text(val textFieldValue: TextFieldValue) : Contents
    data class Image(val imgUrl: String) : Contents
}
