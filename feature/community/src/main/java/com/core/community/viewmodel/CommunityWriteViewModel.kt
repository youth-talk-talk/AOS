package com.core.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.community.model.CommunityWriteUiEffect
import com.core.community.model.CommunityWriteUiEvent
import com.core.community.model.CommunityWriteUiState
import com.core.community.model.ContentInfo
import com.core.domain.usercase.post.PostCreatePostUseCase
import com.core.domain.usercase.search.GetSearchPoliciesTitleUseCase
import com.youthtalk.model.SearchPolicy
import com.youthtalk.model.WriteInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityWriteViewModel @Inject constructor(
    private val getSearchPoliciesTitleUseCase: GetSearchPoliciesTitleUseCase,
    private val postCreatePostUseCase: PostCreatePostUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<CommunityWriteUiState>(CommunityWriteUiState.Success(contents = listOf(WriteInfo(content = "")).toPersistentList()))
    val uiState = _uiState.asStateFlow()

    var uieffect = MutableSharedFlow<CommunityWriteUiEffect>()
        private set

    private val _focusRequest = MutableSharedFlow<Int>()
    val focusRequest = _focusRequest.asSharedFlow()

    fun uiEvent(event: CommunityWriteUiEvent) {
        when (event) {
            is CommunityWriteUiEvent.SearchPolicies -> searchPolicies(event.search)
            is CommunityWriteUiEvent.SelectPolicy -> selectPolicy(event.policy)
            is CommunityWriteUiEvent.ChangeTitleText -> changeTitleText(event.text)
            is CommunityWriteUiEvent.AddImage -> addUri(event.uri)
            is CommunityWriteUiEvent.ChangeContents -> changeContents(event.contentInfo, event.text)
            is CommunityWriteUiEvent.DeleteImage -> deleteImage(event.index)
            is CommunityWriteUiEvent.DeleteText -> deleteContents()
            is CommunityWriteUiEvent.CreatePost -> createPost(event.type)
        }
    }

    private fun createPost(type: String) {
        val state = _uiState.value
        if (state !is CommunityWriteUiState.Success) return

        viewModelScope.launch {
            postCreatePostUseCase(postType = type, title = state.title, policyId = state.selectPolicy?.policyId, contents = state.contents)
                .onStart {
                    _uiState.value = state.copy(isLoading = true)
                }
                .catch {
                    Log.d("YOON-CHAN", "CommunityWriteViewModel createPost error ${it.message}")
                }
                .collectLatest {
                    _uiState.value = state.copy(isLoading = false)
                    uieffect.emit(CommunityWriteUiEffect.GoDetail(it.postId))
                }
        }
    }

    private fun deleteImage(index: Int) {
        val state = _uiState.value
        if (state !is CommunityWriteUiState.Success) return

        val contents = state.contents.toMutableList()

        if (contents[index].content.isNotEmpty()) {
            contents[index - 1] = contents[index - 1].copy(content = contents[index - 1].content + contents[index].content)
        }
        contents.removeAt(index)

        viewModelScope.launch {
            _uiState.value = state.copy(
                contents = contents.toPersistentList(),
                contentsInfo = ContentInfo(index - 1, contents[index - 1].content.length),
            )
            _focusRequest.emit(index - 1)
        }
    }

    private fun deleteContents() {
        val state = _uiState.value
        if (state !is CommunityWriteUiState.Success) return

        val contentInfo = state.contentsInfo
        val contents = state.contents.toMutableList()
        if (contentInfo.index >= 1) {
            contents.removeAt(contentInfo.index)
            viewModelScope.launch {
                _uiState.value = state.copy(
                    contents = contents.toPersistentList(),
                    contentsInfo = contentInfo.copy(index = contentInfo.index - 1, pos = contents[contentInfo.index - 1].content.length),
                )
                _focusRequest.emit(contentInfo.index - 1)
            }
        }
    }

    private fun changeContents(contentInfo: ContentInfo, text: String) {
        val state = _uiState.value
        if (state !is CommunityWriteUiState.Success) return
        val contents = state.contents.toMutableList()

        if (contentInfo.index in 0 until contents.size) {
            contents[contentInfo.index] = contents[contentInfo.index].copy(content = text)

            _uiState.value = state.copy(
                contents = contents.toPersistentList(),
                contentsInfo = contentInfo,
            )
        }
    }

    private fun addUri(uri: String) {
        val state = _uiState.value
        if (state !is CommunityWriteUiState.Success) return

        viewModelScope.launch {
            val index = state.contentsInfo.index
            val contents = state.contents.toMutableList()
            val addText = if (contents[index].content.isEmpty()) "" else contents[index].content.substring(state.contentsInfo.pos)
            contents[index] = contents[index].copy(
                content = if (contents[index].content.isEmpty()) "" else contents[index].content.substring(0 until state.contentsInfo.pos),
            )
            if (contents.size == index + 1) {
                contents.add(WriteInfo(uri, addText))
            } else {
                contents.add(index + 1, WriteInfo(uri, addText))
            }
            Log.d("YOON-CHAN", "addUri $contents")
            _uiState.value = state.copy(
                contents = contents.toPersistentList(),
            )
        }
    }

    private fun changeTitleText(text: String) {
        val state = _uiState.value
        if (state !is CommunityWriteUiState.Success) return
        _uiState.value = state.copy(
            title = text,
        )
        Log.d("YOON-CHAN", "changeTitleText $text")
    }

    private fun selectPolicy(policy: SearchPolicy?) {
        val state = _uiState.value
        if (state !is CommunityWriteUiState.Success) return

        _uiState.value = state.copy(
            selectPolicy = policy,
            searchPolicies = emptyFlow(),
        )
    }

    private fun searchPolicies(search: String) {
        val state = _uiState.value
        if (state !is CommunityWriteUiState.Success) return

        viewModelScope.launch {
            _uiState.value = state.copy(
                searchPolicies = getSearchPoliciesTitleUseCase(title = search).cachedIn(viewModelScope),
            )
        }
    }
}
