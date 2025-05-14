package com.feature.policy.model.policy

import com.core.base.model.UiEvent
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import com.youthtalk.model.typeenum.SortType
import java.time.LocalDate

sealed interface PolicyUiEvent : UiEvent {
    data object InitData : PolicyUiEvent
    data class SelectedDay(val selectedDay: LocalDate) : PolicyUiEvent
    data class SelectCategory(val category: Category, val sortType: SortType) : PolicyUiEvent
    data class PostRegion(val user: User, val region: Region) : PolicyUiEvent
}
