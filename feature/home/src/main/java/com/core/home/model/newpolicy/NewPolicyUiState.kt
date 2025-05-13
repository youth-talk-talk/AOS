package com.core.home.model.newpolicy

import com.core.base.model.UiState
import com.youthtalk.model.home.NewPolicies
import com.youthtalk.model.typeenum.SortType

data class NewPolicyUiState(
    val isLoading: Boolean,
    val newPolices: NewPolicies,
    val sortType: SortType
) : UiState {
    companion object {
        val initState = NewPolicyUiState(
            isLoading = true,
            newPolices = NewPolicies(
                all = listOf(),
                job = listOf(),
                dwelling = listOf(),
                education = listOf(),
                life = listOf(),
                participation = listOf()
            ),
            sortType = SortType.RECENT
        )
    }
}
