package com.youth.search.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.search.GetRecentListUseCase
import com.core.domain.usercase.search.PostRecentListUseCase
import com.youth.search.model.SearchUiEvent
import com.youth.search.model.SearchUiState
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
class SearchViewModel @Inject constructor(
    private val getRecentListUseCase: GetRecentListUseCase,
    private val postRecentListUseCase: PostRecentListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getRecentListUseCase()
                .map {
                    SearchUiState.Success(
                        recentList = it.toPersistentList(),
                    )
                }
                .catch {
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    fun uiEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.ClickRecently -> clickRecently(event.search)
            is SearchUiEvent.DeleteRecently -> deleteRecently(event.search)
        }
    }

    private fun deleteRecently(search: String) {
        val state = _uiState.value
        if (state !is SearchUiState.Success) return

        val recently = state.recentList.toMutableList()
        recently.remove(search)

        viewModelScope.launch {
            postRecentListUseCase(recently)
            _uiState.value = state.copy(
                recentList = recently.toPersistentList(),
            )
        }
    }

    private fun clickRecently(search: String) {
        val state = _uiState.value
        if (state !is SearchUiState.Success) return

        val recently = state.recentList.toMutableList()
        if (recently.contains(search)) {
            recently.remove(search)
            recently.add(0, search)
        } else {
            if (recently.size >= 5) {
                recently.removeLast()
            }
            recently.add(0, search)
        }
        Log.d("YOON-CHAN", recently.joinToString(","))
        viewModelScope.launch {
            postRecentListUseCase(recently)
            _uiState.value = state.copy(
                recentList = recently.toPersistentList(),
            )
        }
    }
}
