package com.core.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.GetCategoriesUseCase
import com.core.domain.usercase.SetCategoriesUseCase
import com.core.home.model.HomeUiState
import com.youthtalk.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val setCategoriesUseCase: SetCategoriesUseCase,
) : ViewModel() {

    private val _errorHandler = MutableSharedFlow<Throwable>()
    val errorHandler = _errorHandler.asSharedFlow()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCategoriesUseCase()
                .map {
                    HomeUiState.Success(
                        categoryList = it.toPersistentList(),
                    )
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    fun setCategoryCheck(category: Category?) {
        category?.let {
            val state = _uiState.value
            if (state !is HomeUiState.Success) return

            val categories = state.categoryList.toMutableList()
            if (categories.contains(category)) {
                categories.remove(category)
            } else {
                categories.add(category)
            }

            viewModelScope.launch {
                setCategoriesUseCase(categories)
                _uiState.value = HomeUiState.Success(
                    categoryList = categories.toPersistentList(),
                )
            }
        }
    }
}
