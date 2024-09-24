package com.core.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.domain.usercase.ChangeCategoriesUseCase
import com.core.domain.usercase.GetCategoriesUseCase
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.home.GetAllPoliciesUseCase
import com.core.domain.usercase.home.GetHomePolicyMapUseCase
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val changeCategoriesUseCase: ChangeCategoriesUseCase,
    private val getAllPoliciesUseCase: GetAllPoliciesUseCase,
    private val getPopularPoliciesUseCase: GetPopularPoliciesUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
    private val getHomePolicyMapUseCase: GetHomePolicyMapUseCase,
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
                getHomePolicyMapUseCase(),
            ) { categories, policies, map ->
                HomeUiState.Success(
                    categoryList = categories.toPersistentList(),
                    popularPolicies = policies.toPersistentList(),
                    allPolicies = getAllPoliciesUseCase().cachedIn(viewModelScope),
                    scrap = map,
                )
            }
                .map {
                    it
                }
                .catch {
                    Timber.e("Home Init error " + it.message)
                }
                .collectLatest {
                    Timber.e("init CollectLatest")
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

    fun postScrap(id: String, isScrap: Boolean) {
        val state = uiState.value
        if (state !is HomeUiState.Success) return

        viewModelScope.launch {
            postPolicyScrapUseCase(id, isScrap)
                .catch {
                    Timber.e("HomeViewModel postScrap error " + it.message)
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        scrap = it,
                    )
                }
        }
    }

    fun onResume() {
        val state = uiState.value
        if (state !is HomeUiState.Success) return
        viewModelScope.launch {
            combine(
                getPopularPoliciesUseCase(),
                getHomePolicyMapUseCase(),
            ) { popular, map ->
                state.copy(
                    popularPolicies = popular.toPersistentList(),
                    scrap = map,
                )
            }
                .catch {
                    Timber.e("Home Init error " + it.message)
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }
}
