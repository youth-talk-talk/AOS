package com.youthtalk.specpolicy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.specpolicy.GetPolicyCountUseCase
import com.core.domain.usercase.specpolicy.GetUserFilterInfoUseCase
import com.core.domain.usercase.specpolicy.PostSpecPoliciesUseCase
import com.core.domain.usercase.specpolicy.SaveFilterUseCase
import com.youthtalk.model.Category
import com.youthtalk.model.EmploymentCode
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecPolicyViewModel @Inject constructor(
    private val postSpecPoliciesUseCase: PostSpecPoliciesUseCase,
    private val getPolicyCountUseCase: GetPolicyCountUseCase,
    private val getUserFilterInfoUseCase: GetUserFilterInfoUseCase,
    private val saveFilterUseCase: SaveFilterUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SpecPolicyUiState>(SpecPolicyUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val event = MutableSharedFlow<SpecPolicyUiEffect>()

    fun uiEvent(event: SpecPolicyUiEvent) {
        when (event) {
            is SpecPolicyUiEvent.GetData -> getData(event.category)
            is SpecPolicyUiEvent.ChangeEmployCode -> changeEmployCode(event.employmentCode)
            is SpecPolicyUiEvent.GetFilterInfo -> getFilterInfo()
            is SpecPolicyUiEvent.ChangeFinished -> changeFinished(event.isFinished)
            is SpecPolicyUiEvent.FilterReset -> resetFilter()
            is SpecPolicyUiEvent.FilterApply -> applyFilter()
            is SpecPolicyUiEvent.ChangeAge -> changeAge(event.age)
            is SpecPolicyUiEvent.ClickScrap -> clickScrap(event.id)
        }
    }

    private fun clickScrap(id: String) {
        val state = _uiState.value
        if (state !is SpecPolicyUiState.Success) return

        viewModelScope.launch {
            postPolicyScrapUseCase(id)
                .catch {
                    Log.d("YOON-CHAN", "SpecPolicyViewModel clickScrap error ${it.message}")
                }
                .collectLatest {
                    val newSet = if (state.scrap.contains(id)) {
                        state.scrap - id
                    } else {
                        state.scrap + id
                    }
                    _uiState.value = state.copy(
                        scrap = newSet,
                    )
                }
        }
    }

    private fun applyFilter() {
        val state = _uiState.value
        if (state !is SpecPolicyUiState.Success) return

        viewModelScope.launch {
            saveFilterUseCase(state.filterInfo)
                .collectLatest {
                    getData(state.category)
                }
        }
    }

    private fun changeAge(age: String) {
        val state = _uiState.value
        if (state !is SpecPolicyUiState.Success) return
        if (age.length >= 3) return

        _uiState.value = state.copy(
            filterInfo = state.filterInfo.copy(
                age = age.toIntOrNull(),
            ),
        )
    }

    private fun resetFilter() {
        val state = _uiState.value
        if (state !is SpecPolicyUiState.Success) return

        _uiState.value = state.copy(
            filterInfo = state.filterInfo.copy(
                age = null,
                employmentCodeList = null,
                isFinished = null,
            ),
        )
    }

    private fun changeFinished(isFinished: Boolean) {
        val state = _uiState.value
        if (state !is SpecPolicyUiState.Success) return

        if (state.filterInfo.isFinished != isFinished) {
            _uiState.value = state.copy(
                filterInfo = state.filterInfo.copy(
                    isFinished = isFinished,
                ),
            )
        }
    }

    private fun getFilterInfo() {
        val state = _uiState.value
        if (state !is SpecPolicyUiState.Success) return

        viewModelScope.launch {
            getUserFilterInfoUseCase()
                .map {
                    state.copy(filterInfo = it)
                }
                .collectLatest { uiState ->
                    _uiState.value = uiState
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
                    category = category,
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

    private fun changeEmployCode(employCode: EmploymentCode) {
        val state = _uiState.value
        if (state !is SpecPolicyUiState.Success) return

        val employs = state.filterInfo.employmentCodeList?.toMutableList()

        _uiState.value = if (employs == null) {
            state.copy(
                filterInfo = state.filterInfo.copy(
                    employmentCodeList = listOf(employCode),
                ),
            )
        } else {
            if (employCode == EmploymentCode.ALL) {
                state.copy(
                    filterInfo = state.filterInfo.copy(
                        employmentCodeList = null,
                    ),
                )
            } else if (employs.contains(employCode)) {
                val newEmploys = (employs - employCode).ifEmpty { null }
                state.copy(
                    filterInfo = state.filterInfo.copy(
                        employmentCodeList = newEmploys,
                    ),
                )
            } else {
                val newEmploys = if ((employs + employCode).size == EmploymentCode.entries.size - 1) null else (employs + employCode)
                state.copy(
                    filterInfo = state.filterInfo.copy(
                        employmentCodeList = newEmploys,
                    ),
                )
            }
        }
    }
}
