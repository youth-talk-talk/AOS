package com.core.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.domain.usercase.mypage.GetAnnouncesUseCase
import com.core.mypage.model.announce.AnnounceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val getAnnouncesUseCase: GetAnnouncesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnnounceUiState>(AnnounceUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = AnnounceUiState.Success(
                announces = getAnnouncesUseCase().cachedIn(viewModelScope),
            )
        }
    }
}
