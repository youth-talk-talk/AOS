package com.core.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.home.GetHomePolicyMapUseCase
import com.core.domain.usercase.mypage.GetScrapPoliciesUseCase
import com.core.mypage.model.scrappolicy.ScrapPolicyUiEvent
import com.core.mypage.model.scrappolicy.ScrapPolicyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScrapPolicyViewModel @Inject constructor(
    private val getScrapPoliciesUseCase: GetScrapPoliciesUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
    private val getHomePolicyMapUseCase: GetHomePolicyMapUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ScrapPolicyUiState>(ScrapPolicyUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getScrapPoliciesUseCase(),
                getHomePolicyMapUseCase(),
            ) { policy, map ->
                ScrapPolicyUiState.Success(
                    policies = policy.cachedIn(viewModelScope),
                    deleteScrapMap = map,
                )
            }
                .catch {
                    Log.d("YOON-CHAN", "scrapPolicyViewModel init error ${it.message}")
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    fun uiEvent(event: ScrapPolicyUiEvent) {
        when (event) {
            is ScrapPolicyUiEvent.DeleteScrap -> deleteScrap(event.policyId, event.scrap)
        }
    }

    private fun deleteScrap(policyId: String, scrap: Boolean) {
        val state = _uiState.value
        if (state !is ScrapPolicyUiState.Success) return

        viewModelScope.launch {
            postPolicyScrapUseCase(policyId, scrap)
                .catch {
                    Log.d("YOON-CHAN", "scrapPolicyViewModel deleteScrap error ${it.message}")
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        deleteScrapMap = it,
                    )
                }
        }
    }
}
