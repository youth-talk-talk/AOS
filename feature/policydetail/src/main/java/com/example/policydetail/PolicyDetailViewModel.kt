package com.example.policydetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailCommentUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailUseCase
import com.example.policydetail.model.PolicyDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyDetailViewModel @Inject constructor(
    private val getPolicyDetailUseCase: GetPolicyDetailUseCase,
    private val getPolicyDetailCommentUseCase: GetPolicyDetailCommentUseCase,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()

    private val _uiState = MutableStateFlow<PolicyDetailUiState>(PolicyDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getData(policyId: String) {
        viewModelScope.launch {
            combine(
                getPolicyDetailUseCase(policyId),
                getPolicyDetailCommentUseCase(policyId),
                getUserUseCase(),
            ) { policyDetail, comments, user ->
                PolicyDetailUiState.Success(
                    policyDetail = policyDetail,
                    myInfo = user,
                    comments = comments.toPersistentList(),
                )
            }
                .catch {
                    Log.d("YOON-CHAN", "PolicyDetailViewModel getData error ${it.message}")
                    _error.emit(it)
                }
                .collectLatest { state ->
                    _uiState.value = state
                }
        }
    }
}
