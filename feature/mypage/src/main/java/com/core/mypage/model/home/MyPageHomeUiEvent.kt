package com.core.mypage.model.home

import com.youthtalk.model.Region

interface MyPageHomeUiEvent {
    data class ChangeNickname(val nickname: String) : MyPageHomeUiEvent
    data class ChangeRegion(val region: Region) : MyPageHomeUiEvent
    data class Logout(val deleteUser: Boolean) : MyPageHomeUiEvent
}
