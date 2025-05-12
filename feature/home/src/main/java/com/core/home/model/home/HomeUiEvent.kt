package com.core.home.model.home

import com.core.base.model.UiEvent
import com.youthtalk.model.Region
import com.youthtalk.model.User

interface HomeUiEvent : UiEvent {
    data class GetHomeData(val isLoading: Boolean = true) : HomeUiEvent
    data class PostRegion(val user: User, val region: Region) : HomeUiEvent
}
