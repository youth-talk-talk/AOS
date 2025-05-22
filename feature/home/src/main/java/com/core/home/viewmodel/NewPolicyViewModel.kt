package com.core.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.home.GetNewPolicesUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailUseCase
import com.core.home.model.newpolicy.NewPolicyUiEffect
import com.core.home.model.newpolicy.NewPolicyUiEvent
import com.core.home.model.newpolicy.NewPolicyUiState
import com.youthtalk.model.typeenum.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class NewPolicyViewModel @Inject constructor(
    private val getNewPolicesUseCase: GetNewPolicesUseCase,
    private val getPolicyDetailUseCase: GetPolicyDetailUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase
) : BaseViewModel<NewPolicyUiState, NewPolicyUiEvent, NewPolicyUiEffect>(
    initialState = NewPolicyUiState.initState
) {

    init {
        setEvent(NewPolicyUiEvent.GetNewPolices(state.value.sortType))
    }

    override fun handleEvents(event: NewPolicyUiEvent) {
        when (event) {
            is NewPolicyUiEvent.GetNewPolices -> getPolices(event.sortType)
            is NewPolicyUiEvent.OnClickPolicy -> {
                setState { copy(policyId = event.policyId) }
                setEffect { NewPolicyUiEffect.ClickPolicy(event.policyId) }
            }

            is NewPolicyUiEvent.OnClickPolicyScrap -> postPolicyScrap(event.policyId, event.scrap)
            is NewPolicyUiEvent.Refresh -> refresh()
        }
    }

    private fun getPolices(sortType: SortType) {
        viewModelScope.launch {
            getNewPolicesUseCase(sortType)
                .onStart {
                    setState { copy(isLoading = true, sortType = sortType) }
                }
                .catch {
                    Timber.e("NewPolicyViewModel getPolices error $it")
                }
                .collectLatest { newPolies ->
                    setState { copy(isLoading = false, newPolicies = newPolies, sortType = sortType) }
                }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            state.value.policyId?.let { policyId ->
                getPolicyDetailUseCase(policyId)
                    .catch {
                        Timber.e("NewPolicyViewModel refresh error $it")
                    }
                    .collectLatest { policyDetail ->
                        setState {
                            copy(
                                newPolicies = newPolicies.copy(
                                    all = newPolicies.all.map { policy ->
                                        if (policy.policyId == policyId && policyDetail.isScrap != policy.scrap) {
                                            policy
                                                .copy(
                                                    scrap = policyDetail.isScrap,
                                                    scrapCount = policy.scrapCount + (if (policyDetail.isScrap) +1 else -1)
                                                )
                                        } else {
                                            policy
                                        }
                                    },
                                    job = newPolicies.job.map { policy ->
                                        if (policy.policyId == policyId && policyDetail.isScrap != policy.scrap) {
                                            policy
                                                .copy(
                                                    scrap = policyDetail.isScrap,
                                                    scrapCount = policy.scrapCount + (if (policyDetail.isScrap) +1 else -1)
                                                )
                                        } else {
                                            policy
                                        }
                                    },
                                    dwelling = newPolicies.dwelling.map { policy ->
                                        if (policy.policyId == policyId && policyDetail.isScrap != policy.scrap) {
                                            policy
                                                .copy(
                                                    scrap = policyDetail.isScrap,
                                                    scrapCount = policy.scrapCount + (if (policyDetail.isScrap) +1 else -1)
                                                )
                                        } else {
                                            policy
                                        }
                                    },
                                    education = newPolicies.education.map { policy ->
                                        if (policy.policyId == policyId && policyDetail.isScrap != policy.scrap) {
                                            policy
                                                .copy(
                                                    scrap = policyDetail.isScrap,
                                                    scrapCount = policy.scrapCount + (if (policyDetail.isScrap) +1 else -1)
                                                )
                                        } else {
                                            policy
                                        }
                                    },
                                    life = newPolicies.life.map { policy ->
                                        if (policy.policyId == policyId && policyDetail.isScrap != policy.scrap) {
                                            policy
                                                .copy(
                                                    scrap = policyDetail.isScrap,
                                                    scrapCount = policy.scrapCount + (if (policyDetail.isScrap) +1 else -1)
                                                )
                                        } else {
                                            policy
                                        }
                                    },
                                    participation = newPolicies.all.map { policy ->
                                        if (policy.policyId == policyId && policyDetail.isScrap != policy.scrap) {
                                            policy
                                                .copy(
                                                    scrap = policyDetail.isScrap,
                                                    scrapCount = policy.scrapCount + (if (policyDetail.isScrap) +1 else -1)
                                                )
                                        } else {
                                            policy
                                        }
                                    }
                                ),
                                policyId = null
                            )
                        }
                    }
            }
        }
    }

    private fun postPolicyScrap(policyId: Long, scrap: Boolean) {
        viewModelScope.launch {
            postPolicyScrapUseCase(policyId, scrap)
                .catch {
                    Timber.e("NewPolicyViewModel postPostScrap error $it")
                }
                .collectLatest {
                    setState {
                        copy(
                            newPolicies = newPolicies.copy(
                                all = newPolicies.all.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                job = newPolicies.job.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                dwelling = newPolicies.dwelling.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                education = newPolicies.education.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                life = newPolicies.life.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                participation = newPolicies.all.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                }
                            )
                        )
                    }
                }
        }
    }
}
