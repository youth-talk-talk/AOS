package com.core.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.core.base.BaseViewModel
import com.core.home.model.popular.PopularPolicyUiEffect
import com.core.home.model.popular.PopularPolicyUiEvent
import com.core.home.model.popular.PopularPolicyUiState
import javax.inject.Inject
import kotlinx.serialization.json.Json
import timber.log.Timber

class PopularPolicyViewModel @Inject constructor(
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
        Timber.e("handleEvents")
    }
}
