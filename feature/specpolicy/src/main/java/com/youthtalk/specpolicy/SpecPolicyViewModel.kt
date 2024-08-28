package com.youthtalk.specpolicy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.domain.usercase.specpolicy.GetPolicyCountUseCase
import com.core.domain.usercase.specpolicy.GetUserFilterInfoUseCase
import com.core.domain.usercase.specpolicy.PostSpecPoliciesUseCase
import com.youthtalk.model.Category
import com.youthtalk.specpolicy.model.SpecPolicyUiEffect
import com.youthtalk.specpolicy.model.SpecPolicyUiEvent
import com.youthtalk.specpolicy.model.SpecPolicyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecPolicyViewModel @Inject constructor(
    private val postSpecPoliciesUseCase: PostSpecPoliciesUseCase,
    private val getPolicyCountUseCase: GetPolicyCountUseCase,
    private val getUserFilterInfoUseCase: GetUserFilterInfoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SpecPolicyUiState>(SpecPolicyUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val event = MutableSharedFlow<SpecPolicyUiEffect>()

    fun uiEvent(event: SpecPolicyUiEvent) {
        when (event) {
            is SpecPolicyUiEvent.GetData -> {
                getData(event.category)
            }
        }
    }

    private fun getData(category: Category) {
        viewModelScope.launch {
            combine(
                postSpecPoliciesUseCase(listOf(category)),
                getPolicyCountUseCase(listOf(category)),
                getUserFilterInfoUseCase(),
            ) { polices, count, filterInfo ->
                SpecPolicyUiState.Success(
                    polices = polices.cachedIn(viewModelScope),
                    policyCount = count,
                    filterInfo = filterInfo,
                )
            }
                .catch {
                    Log.d("YOON-CHAN", "SpecPolicyViewModel erro ${it.message}")
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }
}
