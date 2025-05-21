package com.core.mypage.model.setting

import com.core.base.model.UiEvent
import com.youthtalk.model.typeenum.Region
import java.io.File

sealed interface SettingUiEvent : UiEvent {
    data object InitData : SettingUiEvent
    data class ChangeSettingType(val settingType: SettingType) : SettingUiEvent
    data class PostLogout(val deleteUser: Boolean = false) : SettingUiEvent
    data class SelectImageUrl(val imageUrl: String) : SettingUiEvent
    data class OnChangeValue(val nickname: String) : SettingUiEvent
    data class OnChangeRegion(val region: Region) : SettingUiEvent
    data class OnSaveUser(val file: File? = null) : SettingUiEvent
}
