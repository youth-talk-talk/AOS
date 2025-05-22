package com.core.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailUseCase
import com.core.home.model.popular.PopularPolicyUiEffect
import com.core.home.model.popular.PopularPolicyUiEvent
import com.core.home.model.popular.PopularPolicyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber

@HiltViewModel
class PopularPolicyViewModel @Inject constructor(
    private val getPolicyDetailUseCase: GetPolicyDetailUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<PopularPolicyUiState, PopularPolicyUiEvent, PopularPolicyUiEffect>(
    initialState = PopularPolicyUiState.initState
) {

    init {
        setState {
            copy(
                isLoading = false,
                policies = Json.decodeFromString(savedStateHandle.get<String>("policies") ?: "")
            )
        }
    }

    override fun handleEvents(event: PopularPolicyUiEvent) {
        when (event) {
            is PopularPolicyUiEvent.OnClickPolicy -> {
                setState { copy(policyId = event.policyId) }
                setEffect { PopularPolicyUiEffect.ClickPolicy(event.policyId) }
            }

            is PopularPolicyUiEvent.OnClickPolicyScrap -> postPolicyScrap(event.policyId, event.scrap)
            is PopularPolicyUiEvent.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            state.value.policyId?.let { policyId ->
                getPolicyDetailUseCase(policyId)
                    .catch {
                        Timber.e("PopularPolicyViewModel refresh error $it")
                    }
                    .collectLatest { policyDetail ->
                        setState {
                            copy(
                                policies = policies.map { policy ->
                                    if (policyId == policy.policyId && policyDetail.isScrap != policy.scrap) {
                                        policy.copy(
                                            scrap = policyDetail.isScrap,
                                            scrapCount = policy.scrapCount + (if (policyDetail.isScrap) +1 else -1)
                                        )
                                    } else {
                                        policy
                                    }
                                },
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
                    Timber.e("PopularPolicyViewModel postPostScrap error $it")
                }
                .collectLatest {
                    setState {
                        copy(
                            policies = policies.map { policy ->
                                if (policy.policyId == policyId) {
                                    policy.copy(
                                        scrap = !scrap,
                                        scrapCount = policy.scrapCount + (if (scrap) -1 else 1)
                                    )
                                } else {
                                    policy
                                }
                            }
                        )
                    }
                }
        }
    }
}
