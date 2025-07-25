package com.feature.policy.viewmodel

import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.policy.DeleteAllRecentlyViewPoliciesUseCase
import com.core.domain.usercase.policy.GetRecentlyViewPolicesUseCase
import com.feature.policy.model.recentlyview.RecentlyViewUiEffect
import com.feature.policy.model.recentlyview.RecentlyViewUiEvent
import com.feature.policy.model.recentlyview.RecentlyViewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class RecentlyViewPolicyViewModel @Inject constructor(
    private val getRecentlyViewPolicesUseCase: GetRecentlyViewPolicesUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
    private val deleteAllRecentlyViewPoliciesUseCase: DeleteAllRecentlyViewPoliciesUseCase
) : BaseViewModel<RecentlyViewUiState, RecentlyViewUiEvent, RecentlyViewUiEffect>(
    initialState = RecentlyViewUiState.initState
) {
    init {
        setEvent(RecentlyViewUiEvent.InitData)
    }

    override fun handleEvents(event: RecentlyViewUiEvent) {
        when (event) {
            is RecentlyViewUiEvent.InitData -> initData()
            is RecentlyViewUiEvent.DeleteAll -> deleteAll()
            is RecentlyViewUiEvent.PostScrapPolicy -> postPolicyScrap(event.policyId, event.scrap)
            is RecentlyViewUiEvent.OnClickPolicy -> setEffect { RecentlyViewUiEffect.OnPolicyDetail(event.policyId) }
        }
    }

    private fun deleteAll() {
        viewModelScope.launch {
            deleteAllRecentlyViewPoliciesUseCase()
                .catch {
                    Timber.e("RecentlyViewPolicyViewModel deleteAll error $it")
                }
                .collectLatest {
                    setState { copy(policies = listOf()) }
                }
        }
    }

    private fun postPolicyScrap(policyId: Long, scrap: Boolean) {
        viewModelScope.launch {
            postPolicyScrapUseCase(policyId, scrap)
                .catch {
                    Timber.e("RecentlyViewPolicyViewModel postPolicyScrap error $it")
                }
                .collectLatest {
                    Timber.e("RecentlyViewPolicyViewModel postPolicyScrap success $it")
                    setState {
                        copy(
                            policies = policies
                                .map { policy ->
                                    if (policy.policyId == policyId) policy.copy(scrap = !scrap) else policy
                                }
                        )
                    }
                }
        }
    }

    private fun initData() {
        viewModelScope.launch {
            getRecentlyViewPolicesUseCase()
                .onSuccess {
                    setState { copy(isLoading = false, policies = it) }
                }.onFailure {
                    Timber.e("error : $it")
                }
        }
    }
}
