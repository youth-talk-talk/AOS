package com.core.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.home.screen.HomeScreen
import com.core.navigation.navigator.HomeTabNavigation

fun NavController.navigateHomeTab(navOptions: NavOptions) {
    navigate(HomeTabNavigation.Home, navOptions)
}

fun NavGraphBuilder.homeTabNavigation(onClickPolicySearch: () -> Unit) {
    composable<HomeTabNavigation.Home> {
        HomeScreen(
            onClickPolicySearch = onClickPolicySearch,
        )
    }
}
