package com.core.community.model.write

import androidx.compose.ui.text.input.TextFieldValue
import com.core.base.model.UiEvent
import com.youthtalk.model.policy.SearchPolicy
import java.io.File

sealed interface CommunityWriteUiEvent : UiEvent {
    data class OnTextChangeValue(val index: Int, val text: TextFieldValue) : CommunityWriteUiEvent
    data object GetImages : CommunityWriteUiEvent
    data class PostUploadImages(val file: File) : CommunityWriteUiEvent
    data class FocusChange(val index: Int, val text: TextFieldValue?) : CommunityWriteUiEvent
    data class ChangeTitle(val title: String) : CommunityWriteUiEvent
    data class SearchPolicyChangeTextValue(val searchPolicy: String) : CommunityWriteUiEvent
    data class PostSearchPolicy(val searchPolicy: String) : CommunityWriteUiEvent
    data object ClearSearchInfo : CommunityWriteUiEvent
    data class OnClickSearchPolicy(val search: SearchPolicy) : CommunityWriteUiEvent
    data object PostCreatePost : CommunityWriteUiEvent
}
