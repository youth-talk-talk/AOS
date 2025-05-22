package com.core.mypage.model.setting

import com.core.base.model.UiEffect

sealed interface SettingUiEffect : UiEffect {
    data object Logout : SettingUiEffect
}
