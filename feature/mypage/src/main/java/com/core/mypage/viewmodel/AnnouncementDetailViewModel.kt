package com.core.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.mypage.GetAnnounceDetailUseCase
import com.core.mypage.model.announcedetail.AnnounceDetailUiEvent
import com.core.mypage.model.announcedetail.AnnounceDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AnnouncementDetailViewModel @Inject constructor(
    private val getAnnounceDetailUseCase: GetAnnounceDetailUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AnnounceDetailUiState>(AnnounceDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun uiEvent(event: AnnounceDetailUiEvent) {
        when (event) {
            is AnnounceDetailUiEvent.GetAnnounceDetail -> getAnnounceDetail(event.id)
        }
    }

    private fun getAnnounceDetail(id: Long) {
        viewModelScope.launch {
            getAnnounceDetailUseCase(id)
                .map {
                    AnnounceDetailUiState.Success(
                        announceDetail = it,
                    )
                }
                .catch {
                    Timber.e("AnnouncementDetailViewModel getAnnounceDetail " + it.message)
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }
}
