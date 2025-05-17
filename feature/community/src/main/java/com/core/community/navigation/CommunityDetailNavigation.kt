package com.core.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.community.screen.detail.CommunityDetailScreen
import com.core.navigation.navigator.CommunityDetail

fun NavController.navigateCommunityDetail(postId: Long, navOptions: NavOptions? = null) {
    navigate(CommunityDetail(postId), navOptions)
}

fun NavGraphBuilder.communityDetailNavigation(showSnackBar: (String) -> Unit, onBack: () -> Unit) {
    composable<CommunityDetail> {
        CommunityDetailScreen(
            showSnackBar = showSnackBar,
            onBack = onBack
        )
    }
}
