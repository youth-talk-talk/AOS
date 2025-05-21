package com.feature.policy.model.recentlyview

import com.core.base.model.UiState
import com.youthtalk.model.policy.Policy

data class RecentlyViewUiState(
    val isLoading: Boolean,
    val policies: List<Policy>
) : UiState {
    companion object {
        val initState = RecentlyViewUiState(
            isLoading = true,
            policies = listOf()
        )
    }
}
