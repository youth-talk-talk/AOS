package com.core.mypage.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.domain.usercase.PostPostScrapUseCase
import com.core.domain.usercase.mypage.GetMyPagePostsUseCase
import com.core.mypage.model.posts.MyPagePostsUiEvent
import com.core.mypage.model.posts.MyPagePostsUiState
import com.youthtalk.model.PostType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPagePostViewModel @Inject constructor(
    private val getMyPagePostsUseCase: GetMyPagePostsUseCase,
    private val postScrapUseCase: PostPostScrapUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyPagePostsUiState>(MyPagePostsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        val type = savedStateHandle.get<String>("type") ?: "scrap"
        getPost(type)
    }

    fun uiEvent(event: MyPagePostsUiEvent) {
        when (event) {
            is MyPagePostsUiEvent.PostScrap -> postScrap(event.id, event.scrap, event.type)
        }
    }

    private fun postScrap(id: Long, scrap: Boolean, type: PostType) {
        val state = _uiState.value
        if (state !is MyPagePostsUiState.Success) return

        viewModelScope.launch {
            postScrapUseCase(id, scrap, type)
                .catch {
                    Timber.e("MyPagePostViewModel postScrap error " + it.message)
                }
                .collectLatest {
                    Timber.d("MyPagePostViewModel postScrap $it")
                }
        }
    }

    private fun getPost(type: String) {
        viewModelScope.launch {
            _uiState.value = MyPagePostsUiState.Success(
                posts = getMyPagePostsUseCase(type).cachedIn(viewModelScope),
            )
        }
    }
}
