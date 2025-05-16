package com.core.community.model.write

import androidx.compose.ui.text.input.TextFieldValue
import com.core.base.model.UiEvent
import java.io.File

sealed interface CommunityWriteUiEvent : UiEvent {
    data class OnTextChangeValue(val index: Int, val text: TextFieldValue) : CommunityWriteUiEvent
    data object GetImages : CommunityWriteUiEvent
    data class PostUploadImages(val file: File) : CommunityWriteUiEvent
    data class FocusChange(val index: Int, val text: TextFieldValue?) : CommunityWriteUiEvent
}
