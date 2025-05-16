package com.core.community.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import com.core.base.BaseViewModel
import com.core.community.model.Contents
import com.core.community.model.write.CommunityWriteUiEffect
import com.core.community.model.write.CommunityWriteUiEvent
import com.core.community.model.write.CommunityWriteUiState
import com.youthtalk.model.post.PostSubject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CommunityWriteUiState, CommunityWriteUiEvent, CommunityWriteUiEffect>(
    initialState = CommunityWriteUiState.initState
) {

    val permissions = mutableStateListOf<String>()

    init {
        val communityType = savedStateHandle.get<PostSubject>("communityType") ?: PostSubject.REVIEW
        setState { copy(postType = communityType) }
    }

    override fun handleEvents(event: CommunityWriteUiEvent) {
        when (event) {
            is CommunityWriteUiEvent.OnTextChangeValue -> textChangeValue(event.index, event.text)
        }
    }

    private fun textChangeValue(index: Int, text: TextFieldValue) {
        val contents = state.value.contentList.toMutableList()
        val content = contents[index]
        if (content !is Contents.Text) return

        contents.set(index = index, content.copy(textFieldValue = text))
        setState { copy(contentList = contents) }
    }

    fun dismissDialog() {
        permissions.removeAll(permissions)
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && !permissions.contains(permission)) {
            permissions.add(permission)
        }
    }
}
