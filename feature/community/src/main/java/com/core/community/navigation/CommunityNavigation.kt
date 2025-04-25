package com.core.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.community.screen.community.NewCommunityScreen
import com.core.navigation.navigator.HomeTabNavigation

fun NavController.navigateCommunity(navOptions: NavOptions) {
    navigate(HomeTabNavigation.Home, navOptions)
}

fun NavGraphBuilder.communityNavigation() {
    composable<HomeTabNavigation.Community> {
        NewCommunityScreen()
    }
}
