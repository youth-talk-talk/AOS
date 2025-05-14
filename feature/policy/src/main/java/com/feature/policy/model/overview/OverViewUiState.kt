package com.feature.policy.model.overview

import androidx.paging.PagingData
import com.core.base.model.UiState
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class OverViewUiState(
    val category: Category,
    val sortType: SortType,
    val count: Int,
    val policies: Flow<PagingData<Policy>>
) : UiState {
    companion object {
        val initState = OverViewUiState(
            category = Category.ALL,
            sortType = SortType.RECENT,
            count = 0,
            policies = emptyFlow()
        )
    }
}
