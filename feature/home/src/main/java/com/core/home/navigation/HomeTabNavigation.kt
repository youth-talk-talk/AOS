package com.core.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.home.HomeScreen
import com.core.navigation.navigator.HomeTabNavigation

fun NavController.navigateHomeTab(navOptions: NavOptions) {
    navigate(HomeTabNavigation.Home, navOptions)
}

fun NavGraphBuilder.homeTabNavigation() {
    composable<HomeTabNavigation.Home> {
        HomeScreen()
    }
}
