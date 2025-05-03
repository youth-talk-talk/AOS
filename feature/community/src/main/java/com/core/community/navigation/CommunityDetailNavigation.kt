package com.core.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.community.screen.detail.CommunityDetailScreen
import com.core.navigation.navigator.CommunityDetail

fun NavController.navigateCommunityDetail(navOptions: NavOptions? = null) {
    navigate(CommunityDetail, navOptions)
}

fun NavGraphBuilder.communityDetailNavigation() {
    composable<CommunityDetail> {
        CommunityDetailScreen()
    }
}
