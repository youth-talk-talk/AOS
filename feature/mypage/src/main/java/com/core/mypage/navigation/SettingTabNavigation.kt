package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.core.mypage.screen.SettingScreen
import com.core.navigation.model.CommentType
import com.core.navigation.model.ScrapPostType
import com.core.navigation.navigator.Account
import com.core.navigation.navigator.HomeTabNavigation

fun NavController.navigateSetting(navOptions: NavOptions? = null) {
    navigate(HomeTabNavigation.Setting, navOptions)
}

fun NavController.navigateAccount(navOptions: NavOptions? = null) {
    navigate(Account, navOptions)
}

fun NavGraphBuilder.settingTabNavigation(
    onClickEtc: () -> Unit,
    onClickTerms: () -> Unit,
    onClickSettingScrap: () -> Unit,
    onClickSettingPost: (ScrapPostType) -> Unit,
    onClickSettingComment: (CommentType) -> Unit,
    onClickSettingNotification: () -> Unit,
    goLogin: () -> Unit,
    checkPermission: (String) -> Boolean
) {
    composable<HomeTabNavigation.Setting> {
        SettingScreen(
            onClickEtc = onClickEtc,
            onClickTerms = onClickTerms,
            onClickSettingScrap = onClickSettingScrap,
            onClickSettingPost = onClickSettingPost,
            onClickSettingComment = onClickSettingComment,
            onClickSettingNotification = onClickSettingNotification,
            goLogin = goLogin,
            checkPermission = checkPermission
        )
    }
}
