package com.core.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.mypage.GetMyPageCommentsUseCase
import com.core.mypage.model.comments.MyPageCommentsUiEvent
import com.core.mypage.model.comments.MyPageCommentsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageCommentsViewModel @Inject constructor(
    private val getMyPageCommentsUseCase: GetMyPageCommentsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyPageCommentsUiState>(MyPageCommentsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun uiEvent(event: MyPageCommentsUiEvent) {
        when (event) {
            is MyPageCommentsUiEvent.GetData -> getData(event.isMine)
        }
    }

    private fun getData(isMine: Boolean) {
        viewModelScope.launch {
            getMyPageCommentsUseCase(isMine)
                .map {
                    MyPageCommentsUiState.Success(
                        comments = it.toPersistentList(),
                    )
                }
                .catch {
                    Log.d("YOON-CHAN", "MyPageCommentsViewModel getData error ${it.message}")
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }
}
