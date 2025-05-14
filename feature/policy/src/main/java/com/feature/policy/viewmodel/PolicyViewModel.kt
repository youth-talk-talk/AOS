package com.feature.policy.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.base.BaseViewModel
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.policy.GetRecentlyViewPolicesUseCase
import com.core.domain.usercase.specpolicy.GetPolicyCountUseCase
import com.core.domain.usercase.specpolicy.PostSpecPoliciesUseCase
import com.core.domain.usercase.user.PostUserUseCase
import com.feature.policy.model.policy.PolicyUiEffect
import com.feature.policy.model.policy.PolicyUiEvent
import com.feature.policy.model.policy.PolicyUiState
import com.youthtalk.model.User
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
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
class PolicyViewModel @Inject constructor(
    private val getRecentlyViewPolicesUseCase: GetRecentlyViewPolicesUseCase,
    private val postSpecPoliciesUseCase: PostSpecPoliciesUseCase,
    private val getPolicyCountUseCase: GetPolicyCountUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postUserUseCase: PostUserUseCase
) : BaseViewModel<PolicyUiState, PolicyUiEvent, PolicyUiEffect>(
    initialState = PolicyUiState.initState
) {

    init {
        Timber.e("PolicyViewModel init")
        setEvent(PolicyUiEvent.InitData)
    }

    override fun handleEvents(event: PolicyUiEvent) {
        when (event) {
            is PolicyUiEvent.InitData -> initData()
            is PolicyUiEvent.SelectedDay -> changeSelectedDay(event.selectedDay)
            is PolicyUiEvent.SelectCategory -> changeCategoryPolicies(event.category, event.sortType)
            is PolicyUiEvent.PostRegion -> postUser(event.user, event.region)
        }
    }

    private fun postUser(user: User, region: Region) {
        viewModelScope.launch {
            postUserUseCase(user.nickname, region)
                .catch {
                    Timber.e("HomeViewModel postUser error $it")
                }
                .collectLatest {
                    setState { copy(user = it) }
                }
        }
    }

    private fun changeCategoryPolicies(category: Category, sortType: SortType) {
        val filter = if (category == Category.ALL) null else listOf(category)
        viewModelScope.launch {
            combine(
                postSpecPoliciesUseCase(SearchFilter(category = filter), PolicyType.POLICY_TAB_CATEGORY, sortType),
                getPolicyCountUseCase(SearchFilter(category = filter), sortType)
            ) { categoryPolicies, allCount ->
                Pair(categoryPolicies, allCount)
            }
                .onStart {
                    setState {
                        copy(
                            selectCategory = category,
                            sortType = sortType
                        )
                    }
                }
                .catch {
                    Timber.e("PolicyViewModel changeCategoryPolicies $it")
                }
                .collectLatest { (policies, count) ->
                    setState {
                        copy(
                            categoryPolicies = policies.cachedIn(viewModelScope),
                            allCount = count
                        )
                    }
                }
        }
    }

    private fun changeSelectedDay(selectedDay: LocalDate) {
        val selected = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(selectedDay)
        viewModelScope.launch {
            combine(
                postSpecPoliciesUseCase(SearchFilter(applyDue = selected), PolicyType.POLICY_TAB_DEADLINE),
                getPolicyCountUseCase(SearchFilter(applyDue = selected))
            ) { specPolicies, count ->
                Pair(specPolicies, count)
            }
                .onStart {
                    setState { copy(selectedDay = selectedDay) }
                }
                .catch {
                    Timber.e("PolicyViewModel changeSelectedDay $it")
                }
                .collectLatest { (deadlinePolicies, count) ->
                    setState {
                        copy(
                            deadlineCount = count,
                            deadlinePolicies = deadlinePolicies.cachedIn(viewModelScope)
                        )
                    }
                }
        }
    }

    private fun initData() {
        val today = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(state.value.selectedDay)
        viewModelScope.launch {
            combine(
                getRecentlyViewPolicesUseCase(),
                getUserUseCase(),
                combine(
                    postSpecPoliciesUseCase(SearchFilter(applyDue = today), PolicyType.POLICY_TAB_DEADLINE),
                    getPolicyCountUseCase(SearchFilter(applyDue = today))
                ) { deadlinePolicies, count ->
                    Pair(deadlinePolicies, count)
                },
                combine(
                    postSpecPoliciesUseCase(SearchFilter(), PolicyType.POLICY_TAB_CATEGORY),
                    getPolicyCountUseCase(SearchFilter())
                ) { categoryPolicies, allCount ->
                    Pair(categoryPolicies, allCount)
                }
            ) { recentlyViewPolicies, user, deadlineInfo, categoryInfo ->
                PolicyUiState.initState.copy(
                    user = user,
                    recentlyPolicies = recentlyViewPolicies,
                    deadlinePolicies = deadlineInfo.first.cachedIn(viewModelScope),
                    deadlineCount = deadlineInfo.second,
                    allCount = categoryInfo.second,
                    categoryPolicies = categoryInfo.first.cachedIn(viewModelScope)
                )
            }
                .catch {
                    Timber.e("PolicyViewModel initData $it")
                }
                .collectLatest { state ->
                    setState { state }
                }
        }
    }
}
