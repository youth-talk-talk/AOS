package com.core.mypage.navigation

import com.core.mypage.navigation.SettingRoute.ACCOUNT_MANAGE
import com.core.mypage.navigation.SettingRoute.ANNOUNCE
import com.core.mypage.navigation.SettingRoute.ANNOUNCE_DETAIL
import com.core.mypage.navigation.SettingRoute.MY_PAGE_COMMENT
import com.core.mypage.navigation.SettingRoute.MY_PAGE_HOME
import com.core.mypage.navigation.SettingRoute.MY_PAGE_POST
import com.core.mypage.navigation.SettingRoute.NICKNAME_SETTING
import com.core.mypage.navigation.SettingRoute.SCRAP_POLICY

sealed class SettingNavigation(
    val route: String,
) {
    data object MyPageHome : SettingNavigation(
        route = MY_PAGE_HOME,
    )

    data object AccountManage : SettingNavigation(
        route = ACCOUNT_MANAGE,
    )

    data object NicknameSetting : SettingNavigation(
        route = NICKNAME_SETTING,
    )

    data object ScrapPolicy : SettingNavigation(
        route = SCRAP_POLICY,
    )

    data object MyPagePost : SettingNavigation(
        route = MY_PAGE_POST,
    )

    data object MyPageComment : SettingNavigation(
        route = MY_PAGE_COMMENT,
    )

    data object Announce : SettingNavigation(
        route = ANNOUNCE,
    )

    data object AnnounceDetail : SettingNavigation(
        route = ANNOUNCE_DETAIL,
    )
}

object SettingRoute {
    const val MY_PAGE_HOME = "my_page_home"
    const val ACCOUNT_MANAGE = "account_manage"
    const val NICKNAME_SETTING = "nickname_setting"
    const val SCRAP_POLICY = "scrap_policy"
    const val MY_PAGE_POST = "my_page_post"
    const val MY_PAGE_COMMENT = "my_page_comment"
    const val ANNOUNCE = "announce"
    const val ANNOUNCE_DETAIL = "announce_detail"
}
