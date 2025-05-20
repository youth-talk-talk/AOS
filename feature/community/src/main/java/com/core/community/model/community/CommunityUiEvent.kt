package com.core.community.model.community

import com.core.base.model.UiEvent
import com.youthtalk.model.typeenum.Category

sealed interface CommunityUiEvent : UiEvent {
    data object InitData : CommunityUiEvent
    data class ChangeCategory(val category: Category) : CommunityUiEvent
    data object SyncPostDate : CommunityUiEvent
}
