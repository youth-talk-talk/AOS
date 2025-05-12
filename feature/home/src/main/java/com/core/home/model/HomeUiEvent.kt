package com.core.home.model

import com.core.base.model.UiEvent
import com.youthtalk.model.Region
import com.youthtalk.model.User

interface HomeUiEvent : UiEvent {
    data object GetHomeData : HomeUiEvent
    data class PostRegion(val user: User, val region: Region) : HomeUiEvent
}
