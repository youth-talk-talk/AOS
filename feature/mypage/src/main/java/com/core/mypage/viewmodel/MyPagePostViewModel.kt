package com.core.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyPagePostsUiState>(MyPagePostsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun uiEvent(event: MyPagePostsUiEvent) {
        when (event) {
            is MyPagePostsUiEvent.GetData -> getPost(event.type)
        }
    }

    private fun getPost(type: String) {
        viewModelScope.launch {
            getMyPagePostsUseCase(type)
                .map {
                    MyPagePostsUiState.Success(it)
                }
                .catch {
                    Log.d("YOON-CHAN", "MyPagePostViewModel getPost error ${it.message}")
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }
}
