package com.feature.policy.model.policy

import androidx.paging.PagingData
import com.core.base.model.UiState
import com.youthtalk.model.User
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import com.youthtalk.model.typeenum.SortType
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PolicyUiState(
    val user: User,
    val selectedDay: LocalDate,
    val selectCategory: Category,
    val deadlineCount: Int,
    val allCount: Int,
    val sortType: SortType,
    val recentlyPolicies: List<Policy>,
    val deadlinePolicies: Flow<PagingData<Policy>>,
    val categoryPolicies: Flow<PagingData<Policy>>
) : UiState {
    companion object {
        val initState = PolicyUiState(
            user = User(0, "", null, Region.ALL),
            selectedDay = LocalDate.now(),
            selectCategory = Category.ALL,
            deadlineCount = 0,
            allCount = 0,
            sortType = SortType.RECENT,
            recentlyPolicies = listOf(),
            deadlinePolicies = emptyFlow(),
            categoryPolicies = emptyFlow()
        )
    }
}
