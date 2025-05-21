package com.core.mypage.model.setting

import com.core.base.model.UiState
import com.youthtalk.model.Image
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Region
import java.io.File

data class SettingUiState(
    val user: User,
    val uploadLoading: Boolean,
    val accountUser: User,
    val settingInfoType: SettingType,
    val images: List<Image>,
    val file: File?
) : UiState {
    companion object {
        val initState = SettingUiState(
            user = User(
                memberId = 0,
                nickname = "",
                profileImgUrl = null,
                region = Region.ALL
            ),
            accountUser = User(
                memberId = 0,
                nickname = "",
                profileImgUrl = null,
                region = Region.ALL
            ),
            settingInfoType = SettingType.MAIN,
            images = listOf(),
            uploadLoading = false,
            file = null
        )
    }
}
