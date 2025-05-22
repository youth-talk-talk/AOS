package com.feature.policy.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.base.BaseViewModel
import com.core.domain.usercase.specpolicy.GetPolicyCountUseCase
import com.core.domain.usercase.specpolicy.PostSpecPoliciesUseCase
import com.feature.policy.model.deadline.DeadlineUiEffect
import com.feature.policy.model.deadline.DeadlineUiEvent
import com.feature.policy.model.deadline.DeadlineUiState
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class DeadlineViewmodel @Inject constructor(
    private val postSpecPoliciesUseCase: PostSpecPoliciesUseCase,
    private val getPolicyCountUseCase: GetPolicyCountUseCase
) : BaseViewModel<DeadlineUiState, DeadlineUiEvent, DeadlineUiEffect>(
    initialState = DeadlineUiState.initState
) {

    init {
        setEvent(DeadlineUiEvent.InitData)
    }

    override fun handleEvents(event: DeadlineUiEvent) {
        when (event) {
            is DeadlineUiEvent.InitData -> initData()
            is DeadlineUiEvent.SelectedDay -> changeSelectedDay(event.selectedDay)
            is DeadlineUiEvent.SelectedSortType -> changeSortType(event.sortType)
        }
    }

    private fun changeSortType(sortType: SortType) {
        val selected = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(state.value.selectedDay)
        viewModelScope.launch {
            combine(
                postSpecPoliciesUseCase(SearchFilter(applyDue = selected), PolicyType.DEADLINE, sortType),
                getPolicyCountUseCase(SearchFilter(applyDue = selected), sortType)
            ) { categoryPolicies, allCount ->
                Pair(categoryPolicies, allCount)
            }
                .onStart {
                    setState {
                        copy(sortType = sortType)
                    }
                }
                .catch {
                    Timber.e("DeadlineViewmodel changeSortType error $it")
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

    private fun changeSelectedDay(selectedDay: LocalDate) {
        val selected = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(selectedDay)
        viewModelScope.launch {
            combine(
                postSpecPoliciesUseCase(SearchFilter(applyDue = selected), PolicyType.DEADLINE),
                getPolicyCountUseCase(SearchFilter(applyDue = selected))
            ) { specPolicies, count ->
                Pair(specPolicies, count)
            }
                .onStart {
                    setState { copy(selectedDay = selectedDay) }
                }
                .catch {
                    Timber.e("DeadlineViewmodel changeSelectedDay $it")
                }
                .collectLatest { (deadlinePolicies, count) ->
                    setState {
                        copy(
                            count = count,
                            policies = deadlinePolicies.cachedIn(viewModelScope)
                        )
                    }
                }
        }
    }

    private fun initData() {
        val today = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(state.value.selectedDay)
        viewModelScope.launch {
            combine(
                postSpecPoliciesUseCase(SearchFilter(applyDue = today), PolicyType.DEADLINE),
                getPolicyCountUseCase(SearchFilter(applyDue = today))
            ) { deadlinePolicies, count ->
                Pair(deadlinePolicies, count)
            }
                .catch {
                    Timber.e("DeadlineViewmodel initData error $it")
                }
                .collectLatest { (policies, count) ->
                    setState { copy(policies = policies, count = count) }
                }
        }
    }
}
