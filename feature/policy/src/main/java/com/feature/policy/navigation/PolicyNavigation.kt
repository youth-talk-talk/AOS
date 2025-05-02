package com.feature.policy.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.navigation.navigator.HomeTabNavigation
import com.feature.policy.screen.PolicyScreen

fun NavController.navigatePolicyTab(navOptions: NavOptions) {
    navigate(HomeTabNavigation.Policy, navOptions)
}

fun NavGraphBuilder.policyTabNavigation(onClickRecentViewPolicy: () -> Unit, onClickDeadlinePolicy: () -> Unit) {
    composable<HomeTabNavigation.Policy> {
        PolicyScreen(
            onClickRecentViewPolicy = onClickRecentViewPolicy,
            onClickDeadlinePolicy = onClickDeadlinePolicy,
        )
    }
}
