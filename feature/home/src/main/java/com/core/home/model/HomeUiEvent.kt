package com.core.home.model

import com.core.base.model.UiEvent

interface HomeUiEvent : UiEvent {
    data object GetHomeData : HomeUiEvent
}
