package com.feature.policy.model.deadline

import androidx.paging.PagingData
import com.core.base.model.UiState
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.typeenum.SortType
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class DeadlineUiState(
    val selectedDay: LocalDate,
    val sortType: SortType,
    val count: Int,
    val policies: Flow<PagingData<Policy>>
) : UiState {
    companion object {
        val initState = DeadlineUiState(
            selectedDay = LocalDate.now(),
            sortType = SortType.RECENT,
            count = 0,
            policies = emptyFlow()
        )
    }
}
