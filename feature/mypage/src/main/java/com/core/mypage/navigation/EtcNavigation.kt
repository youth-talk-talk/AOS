package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.mypage.screen.EtcScreen
import com.core.navigation.navigator.Etc

fun NavController.navigateSettingEtc(navOptions: NavOptions? = null) {
    navigate(Etc, navOptions)
}

fun NavGraphBuilder.settingEtcNavigation() {
    composable<Etc> {
        EtcScreen()
    }
}
