package com.feature.policy.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.base.BaseViewModel
import com.core.domain.usercase.specpolicy.GetPolicyCountUseCase
import com.core.domain.usercase.specpolicy.PostSpecPoliciesUseCase
import com.feature.policy.model.overview.OverViewUiEffect
import com.feature.policy.model.overview.OverViewUiEvent
import com.feature.policy.model.overview.OverViewUiState
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class PolicyOverviewViewModel @Inject constructor(
    private val postSpecPoliciesUseCase: PostSpecPoliciesUseCase,
    private val getPolicyCountUseCase: GetPolicyCountUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<OverViewUiState, OverViewUiEvent, OverViewUiEffect>(
    initialState = OverViewUiState.initState
) {

    init {
        val category = savedStateHandle.get<Category>("category") ?: Category.ALL
        Timber.e("category $category")
        setEvent(OverViewUiEvent.InitData(category))
    }

    override fun handleEvents(event: OverViewUiEvent) {
        when (event) {
            is OverViewUiEvent.InitData -> initData(category = event.category)
            is OverViewUiEvent.ChangeCategory -> changeCategory(category = event.category)
            is OverViewUiEvent.SelectedSortType -> changeSortType(category = event.category, sortType = event.sortType)
        }
    }

    private fun changeCategory(category: Category) {
        val filter = if (category == Category.ALL) null else listOf(category)
        viewModelScope.launch {
            combine(
                postSpecPoliciesUseCase(SearchFilter(category = filter), PolicyType.OVERVIEW),
                getPolicyCountUseCase(SearchFilter(category = filter))
            ) { categoryPolicies, allCount ->
                Pair(categoryPolicies, allCount)
            }
                .onStart { setState { copy(category = category) } }
                .catch {
                    Timber.e("PolicyOverviewViewModel changeCategory error $it")
                }
                .collectLatest { (policies, count) ->
                    setState {
                        copy(
                            policies = policies.cachedIn(viewModelScope),
                            count = count
                        )
                    }
                }
        }
    }

    private fun changeSortType(category: Category, sortType: SortType) {
        val filter = if (category == Category.ALL) null else listOf(category)
        viewModelScope.launch {
            combine(
                postSpecPoliciesUseCase(SearchFilter(category = filter), PolicyType.OVERVIEW, sortType = sortType),
                getPolicyCountUseCase(SearchFilter(category = filter), sortType = sortType)
            ) { categoryPolicies, allCount ->
                Pair(categoryPolicies, allCount)
            }
                .onStart { setState { copy(category = category, sortType = sortType) } }
                .catch {
                    Timber.e("PolicyOverviewViewModel changeSortType error $it")
                }
                .collectLatest { (policies, count) ->
                    setState {
                        copy(
                            policies = policies.cachedIn(viewModelScope),
                            count = count
                        )
                    }
                }
        }
    }

    private fun initData(category: Category) {
        val filter = if (category == Category.ALL) null else listOf(category)
        viewModelScope.launch {
            combine(
                postSpecPoliciesUseCase(SearchFilter(category = filter), PolicyType.OVERVIEW),
                getPolicyCountUseCase(SearchFilter(category = filter))
            ) { categoryPolicies, allCount ->
                Pair(categoryPolicies, allCount)
            }
                .onStart { setState { copy(category = category) } }
                .catch {
                    Timber.e("PolicyOverviewViewModel initData error $it")
                }
                .collectLatest { (policies, count) ->
                    setState {
                        copy(
                            policies = policies.cachedIn(viewModelScope),
                            count = count
                        )
                    }
                }
        }
    }
}
