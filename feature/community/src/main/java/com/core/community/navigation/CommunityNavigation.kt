package com.core.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.community.screen.community.NewCommunityScreen
import com.core.navigation.navigator.HomeTabNavigation
import com.youthtalk.model.CommunityType

fun NavController.navigateCommunity(navOptions: NavOptions) {
    navigate(HomeTabNavigation.Home, navOptions)
}

fun NavGraphBuilder.communityNavigation(
    onClickCommunitySearch: (CommunityType) -> Unit,
    onClickPostDetail: () -> Unit,
    onClickCommunityWrite: (CommunityType) -> Unit
) {
    composable<HomeTabNavigation.Community> {
        NewCommunityScreen(
            onClickCommunitySearch = onClickCommunitySearch,
            onClickPostDetail = onClickPostDetail,
            onClickCommunityWrite = onClickCommunityWrite
        )
    }
}
