package com.core.mypage.model.setting

import com.core.base.model.UiEvent
import com.youthtalk.model.typeenum.Region
import java.io.File

sealed interface SettingUiEvent : UiEvent {
    data object InitData : SettingUiEvent
    data class ChangeSettingType(val settingType: SettingType) : SettingUiEvent
    data class PostLogout(val deleteUser: Boolean = false) : SettingUiEvent
    data class PostUploadImages(val file: File) : SettingUiEvent
    data class OnChangeValue(val nickname: String) : SettingUiEvent
    data class OnChangeRegion(val region: Region) : SettingUiEvent
    data object OnSaveUser : SettingUiEvent
}
