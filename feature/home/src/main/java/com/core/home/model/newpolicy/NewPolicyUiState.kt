package com.core.home.model.newpolicy

import com.core.base.model.UiState
import com.youthtalk.model.home.NewPolicies
import com.youthtalk.model.typeenum.SortType

data class NewPolicyUiState(
    val isLoading: Boolean,
    val newPolicies: NewPolicies,
    val sortType: SortType,
    val policyId: Long?
) : UiState {
    companion object {
        val initState = NewPolicyUiState(
            isLoading = true,
            newPolicies = NewPolicies(
                all = listOf(),
                job = listOf(),
                dwelling = listOf(),
                education = listOf(),
                life = listOf(),
                participation = listOf()
            ),
            sortType = SortType.RECENT,
            policyId = null
        )
    }
}
