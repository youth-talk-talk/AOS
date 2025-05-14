package com.core.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.community.screen.community.CommunityScreen
import com.core.navigation.navigator.HomeTabNavigation
import com.youthtalk.model.post.PostSubject

fun NavController.navigateCommunity(navOptions: NavOptions) {
    navigate(HomeTabNavigation.Home, navOptions)
}

fun NavGraphBuilder.communityNavigation(
    onClickCommunitySearch: (PostSubject) -> Unit,
    onClickPostDetail: (Long) -> Unit,
    onClickCommunityWrite: (PostSubject) -> Unit
) {
    composable<HomeTabNavigation.Community> {
        CommunityScreen(
            onClickCommunitySearch = onClickCommunitySearch,
            onClickPostDetail = onClickPostDetail,
            onClickCommunityWrite = onClickCommunityWrite
        )
    }
}
