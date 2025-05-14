package com.feature.policy.model.deadline

import com.core.base.model.UiEvent
import com.youthtalk.model.typeenum.SortType
import java.time.LocalDate

sealed interface DeadlineUiEvent : UiEvent {
    data object InitData : DeadlineUiEvent
    data class SelectedDay(val selectedDay: LocalDate) : DeadlineUiEvent
    data class SelectedSortType(val sortType: SortType) : DeadlineUiEvent
}
