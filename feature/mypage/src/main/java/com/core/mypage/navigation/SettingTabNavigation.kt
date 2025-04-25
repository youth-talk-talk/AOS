package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.mypage.screen.SettingScreen
import com.core.navigation.navigator.HomeTabNavigation

fun NavController.navigateSetting(navOptions: NavOptions) {
    navigate(HomeTabNavigation.Setting, navOptions)
}

fun NavGraphBuilder.settingTabNavigation() {
    composable<HomeTabNavigation.Setting> {
        SettingScreen()
    }
}
