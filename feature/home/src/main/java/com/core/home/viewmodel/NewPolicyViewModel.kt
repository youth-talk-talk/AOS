package com.core.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.home.GetNewPolicesUseCase
import com.core.home.model.newpolicy.NewPolicyUiEffect
import com.core.home.model.newpolicy.NewPolicyUiEvent
import com.core.home.model.newpolicy.NewPolicyUiState
import com.youthtalk.model.enum.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class NewPolicyViewModel @Inject constructor(
    private val getNewPolicesUseCase: GetNewPolicesUseCase
) : BaseViewModel<NewPolicyUiState, NewPolicyUiEvent, NewPolicyUiEffect>(
    initialState = NewPolicyUiState.initState
) {

    init {
        setEvent(NewPolicyUiEvent.GetNewPolices(state.value.sortType))
    }

    override fun handleEvents(event: NewPolicyUiEvent) {
        when (event) {
            is NewPolicyUiEvent.GetNewPolices -> getPolices(event.sortType)
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
                    setState { copy(isLoading = false, newPolices = newPolies, sortType = sortType) }
                }
        }
    }
}
