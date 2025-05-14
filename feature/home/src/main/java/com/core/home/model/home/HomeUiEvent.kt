package com.core.home.model.home

import com.core.base.model.UiEvent
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Region

interface HomeUiEvent : UiEvent {
    data class GetHomeData(val isLoading: Boolean = true) : HomeUiEvent
    data class PostRegion(val user: User, val region: Region) : HomeUiEvent
}
