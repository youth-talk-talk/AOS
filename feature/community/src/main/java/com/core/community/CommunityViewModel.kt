package com.core.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.community.model.CommunityUiState
import com.core.domain.usercase.review.GetReviewCategoriesUseCase
import com.core.domain.usercase.review.SetReviewCategoriesUseCase
import com.youthtalk.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getReviewCategoriesUseCase: GetReviewCategoriesUseCase,
    private val setReviewCategoriesUseCase: SetReviewCategoriesUseCase,
) : ViewModel() {

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()

    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getReviewCategoriesUseCase()
                .map {
                    CommunityUiState.Success(
                        categories = it.toPersistentList(),
                    )
                }
                .catch {
                    Log.d("YOON-CHAN", "CommunityViewModel getReviewCategoriesUseCase error")
                }
                .collectLatest { state ->
                    _uiState.value = state
                }
        }
    }

    fun setCategories(category: Category?) {
        category?.let {
            val state = _uiState.value
            if (state !is CommunityUiState.Success) return

            val categories = state.categories.toMutableList()
            if (categories.contains(category) && categories.size <= 1) return

            if (categories.contains(category)) {
                categories.remove(category)
            } else {
                categories.add(category)
            }
            viewModelScope.launch {
                setReviewCategoriesUseCase(categories)
            }
        }
    }
}
