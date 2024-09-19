package com.core.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.domain.usercase.PostPostScrapUseCase
import com.core.domain.usercase.mypage.GetMyPagePostsUseCase
import com.core.mypage.model.posts.MyPagePostsUiEvent
import com.core.mypage.model.posts.MyPagePostsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPagePostViewModel @Inject constructor(
    private val getMyPagePostsUseCase: GetMyPagePostsUseCase,
    private val postScrapUseCase: PostPostScrapUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyPagePostsUiState>(MyPagePostsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun uiEvent(event: MyPagePostsUiEvent) {
        when (event) {
            is MyPagePostsUiEvent.GetData -> getPost(event.type)
            is MyPagePostsUiEvent.PostScrap -> postScrap(event.id, event.scrap)
        }
    }

    private fun postScrap(id: Long, scrap: Boolean) {
        val state = _uiState.value
        if (state !is MyPagePostsUiState.Success) return

        viewModelScope.launch {
            postScrapUseCase(id, scrap)
                .catch {
                    Log.d("YOON-CHAN", "MyPagePostViewModel postScrap error ${it.message}")
                }
                .collectLatest {
                    val map = if (state.scrapMap.containsKey(id)) {
                        state.scrapMap - id
                    } else {
                        state.scrapMap + Pair(id, !scrap)
                    }
                    _uiState.value = state.copy(
                        scrapMap = map,
                    )
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
