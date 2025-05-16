package com.core.community.model.write

import androidx.compose.ui.text.input.TextFieldValue
import com.core.base.model.UiEvent

sealed interface CommunityWriteUiEvent : UiEvent {
    data class OnTextChangeValue(val index: Int, val text: TextFieldValue) : CommunityWriteUiEvent
}
