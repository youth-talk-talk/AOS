package com.core.home.model.popular

import com.core.base.model.UiState
import com.youthtalk.model.policy.Policy

data class PopularPolicyUiState(
    val isLoading: Boolean,
    val policies: List<Policy>
) : UiState {
    companion object {
        val initState = PopularPolicyUiState(
            isLoading = true,
            policies = listOf()
        )
    }
}
