package com.core.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.domain.usercase.ChangeCategoriesUseCase
import com.core.domain.usercase.GetCategoriesUseCase
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.home.GetAllPoliciesUseCase
import com.core.domain.usercase.home.GetPopularPoliciesUseCase
import com.core.home.model.HomeUiState
import com.youthtalk.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val changeCategoriesUseCase: ChangeCategoriesUseCase,
    private val getAllPoliciesUseCase: GetAllPoliciesUseCase,
    private val getPopularPoliciesUseCase: GetPopularPoliciesUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
) : ViewModel() {

    private val _errorHandler = MutableSharedFlow<Throwable>()
    val errorHandler = _errorHandler.asSharedFlow()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getCategoriesUseCase(),
                getPopularPoliciesUseCase(),
                getAllPoliciesUseCase(),
            ) { categories, policies, all ->
                HomeUiState.Success(
                    categoryList = categories.toPersistentList(),
                    popularPolicies = policies.toPersistentList(),
                    allPolicies = all.cachedIn(viewModelScope),
                )
            }
                .map {
                    it
                }
                .catch {
                    Log.d("YOON-CHAN", "Home Init error ${it.message}")
                }
                .collectLatest {
                    Log.d("YOON-CHAN", "init CollectLatest")
                    _uiState.value = it
                }
        }
    }

    fun changeCategoryCheck(category: Category?) {
        category?.let {
            val state = uiState.value
            if (state !is HomeUiState.Success) return

            val categories = state.categoryList.toMutableList()
            if (categories.contains(category) && categories.size <= 1) return

            if (categories.contains(category)) {
                categories.remove(category)
            } else {
                categories.add(category)
            }

            viewModelScope.launch {
                changeCategoriesUseCase(categories)
            }
        }
    }

    fun postScrap(id: String) {
        val state = uiState.value
        if (state !is HomeUiState.Success) return

        viewModelScope.launch {
            postPolicyScrapUseCase(id)
                .catch {
                    Log.d("YOON-CHAN", "HomeViewModel postScrap error ${it.message}")
                }
                .collectLatest {
                    val newSet = if (state.scrap.contains(id)) state.scrap - id else state.scrap + id
                    _uiState.value = state.copy(
                        scrap = newSet,
                    )
                }
        }
    }
}
