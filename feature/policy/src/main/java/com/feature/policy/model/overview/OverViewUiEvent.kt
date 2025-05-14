package com.feature.policy.model.overview

import com.core.base.model.UiEvent
import com.youthtalk.model.Category
import com.youthtalk.model.typeenum.SortType

sealed interface OverViewUiEvent : UiEvent {
    data class InitData(val category: Category) : OverViewUiEvent
    data class ChangeCategory(val category: Category) : OverViewUiEvent
    data class SelectedSortType(val category: Category, val sortType: SortType) : OverViewUiEvent
}
