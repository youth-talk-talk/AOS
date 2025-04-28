package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.mypage.screen.ScrapPolicyScreen
import com.core.navigation.navigator.ScrapPolicy

fun NavController.navigateSettingScrapPolicy(navOptions: NavOptions? = null) {
    navigate(ScrapPolicy, navOptions)
}

fun NavGraphBuilder.settingScrapPolicyNavigation() {
    composable<ScrapPolicy> {
        ScrapPolicyScreen()
    }
}
