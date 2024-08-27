package com.core.navigation

import com.core.navigation.SettingRoute.ACCOUNT_MANAGE
import com.core.navigation.SettingRoute.ANNOUNCE
import com.core.navigation.SettingRoute.MY_PAGE_COMMENT
import com.core.navigation.SettingRoute.MY_PAGE_HOME
import com.core.navigation.SettingRoute.MY_PAGE_POST
import com.core.navigation.SettingRoute.NICKNAME_SETTING
import com.core.navigation.SettingRoute.SCRAP_POLICY

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

    companion object {
        fun includeSetting(route: String?): Boolean {
            return when (route) {
                MY_PAGE_HOME,
                ACCOUNT_MANAGE,
                NICKNAME_SETTING,
                SCRAP_POLICY,
                MY_PAGE_POST,
                MY_PAGE_COMMENT,
                ANNOUNCE,
                -> true
                else -> false
            }
        }
    }
}

object SettingRoute {
    const val MY_PAGE_HOME = "my_page_home"
    const val ACCOUNT_MANAGE = "account_manage"
    const val NICKNAME_SETTING = "nickname_setting"
    const val SCRAP_POLICY = "scrap_policy"
    const val MY_PAGE_POST = "my_page_post"
    const val MY_PAGE_COMMENT = "my_page_comment"
    const val ANNOUNCE = "announce"
}
