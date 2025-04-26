package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.mypage.screen.TermsScreen
import com.core.navigation.navigator.Terms

fun NavController.navigateSettingTerms(navOptions: NavOptions? = null) {
    navigate(Terms, navOptions)
}

fun NavGraphBuilder.settingTermsNavigation() {
    composable<Terms> {
        TermsScreen()
    }
}
