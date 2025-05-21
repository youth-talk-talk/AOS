package com.feature.policy.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.navigation.navigator.RecentlyViewPolicy
import com.feature.policy.screen.RecentlyViewPolicyScreenRoot

fun NavController.navigateRecentlyViewPolicy(navOptions: NavOptions? = null) {
    navigate(RecentlyViewPolicy, navOptions)
}

fun NavGraphBuilder.recentlyViewPolicyNavigation(onBack: () -> Unit, onClickPolicyDetail: (Long) -> Unit) {
    composable<RecentlyViewPolicy> {
        RecentlyViewPolicyScreenRoot(
            onBack = onBack,
            onClickPolicyDetail = onClickPolicyDetail
        )
    }
}
