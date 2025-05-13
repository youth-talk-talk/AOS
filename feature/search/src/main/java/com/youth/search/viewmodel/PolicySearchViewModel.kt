package com.youth.search.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.base.BaseViewModel
import com.core.domain.usercase.search.GetRecentListUseCase
import com.core.domain.usercase.search.PostRecentListUseCase
import com.core.domain.usercase.specpolicy.GetPolicyCountUseCase
import com.core.domain.usercase.specpolicy.PostSpecPoliciesUseCase
import com.youth.search.model.SearchState
import com.youth.search.model.policysearch.PolicySearchUiEffect
import com.youth.search.model.policysearch.PolicySearchUiEvent
import com.youth.search.model.policysearch.PolicySearchUiState
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.SortType
import com.youthtalk.util.SpecializedUtils.getFilterList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class PolicySearchViewModel @Inject constructor(
    val getRecentListUseCase: GetRecentListUseCase,
    val postRecentListUseCase: PostRecentListUseCase,
    val getPolicyCountUseCase: GetPolicyCountUseCase,
    val postSpecPoliciesUseCase: PostSpecPoliciesUseCase
) : BaseViewModel<PolicySearchUiState, PolicySearchUiEvent, PolicySearchUiEffect>(
    initialState = PolicySearchUiState.initState
) {
    init {
        setEvent(PolicySearchUiEvent.InitData)
    }

    override fun handleEvents(event: PolicySearchUiEvent) {
        when (event) {
            is PolicySearchUiEvent.InitData -> initData()
            is PolicySearchUiEvent.SetRecently -> setRecentlyListUseCase(event.recently)
            is PolicySearchUiEvent.SetState -> setState(event.state)
            is PolicySearchUiEvent.Search -> search(event.search)
            is PolicySearchUiEvent.SetFilter -> postSpecPolicies(event.searchFilter, event.sortType)
        }
    }

    private fun postSpecPolicies(searchFilter: SearchFilter, sortType: SortType) {
        val search = searchFilter.copy(
            specialization = getFilterList(searchFilter.specialization)
        )
        viewModelScope.launch {
            Timber.e("PolicySearchViewModel search start")
            combine(
                getPolicyCountUseCase(search, sortType),
                postSpecPoliciesUseCase(search, PolicyType.SEARCH, sortType)
            ) { count, policies ->
                Pair(count, policies)
            }
                .onStart {
                    setState { copy(searchLoading = true) }
                }
                .catch {
                    Timber.e("PolicySearchViewModel search error $it")
                    setState { copy(searchLoading = false) }
                }
                .collectLatest { (count, policies) ->
                    Timber.e("PolicySearchViewModel search success $count $policies")
                    setState {
                        copy(
                            searchLoading = false,
                            count = count,
                            policies = policies.cachedIn(viewModelScope),
                            searchFilter = searchFilter,
                            sortType = sortType
                        )
                    }
                }
        }
    }

    private fun search(search: String) {
        viewModelScope.launch {
            setState(SearchState.SEARCH)
            val list = state.value.recently.toMutableList()
            if (list.contains(search)) {
                list.remove(search)
                list.add(0, search)
            } else {
                list.add(search)
            }
            setRecentlyListUseCase(list)

            setState { copy(searchFilter = searchFilter.copy(keyword = search)) }
            postSpecPolicies(state.value.searchFilter, state.value.sortType)
        }
    }

    private fun setState(state: SearchState) {
        viewModelScope.launch {
            setState { copy(state = state) }
        }
    }

    private fun setRecentlyListUseCase(recently: List<String>) {
        Timber.e("setRecentlyListUseCase recently $recently")
        viewModelScope.launch {
            postRecentListUseCase(recently)
        }
    }

    private fun initData() {
        viewModelScope.launch {
            getRecentListUseCase()
                .catch {
                    Timber.e("PolicySearchViewModel initData error $it")
                }
                .collectLatest {
                    Timber.e("PolicySearchViewModel initData success $it")
                    setState { copy(recently = it) }
                }
        }
    }
}
