package com.feature.policy.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.navigation.navigator.DeadlinePolicy
import com.feature.policy.screen.DeadlinePolicyScreen

fun NavController.navigateDeadlinePolicy(navOptions: NavOptions? = null) {
    navigate(DeadlinePolicy, navOptions)
}

fun NavGraphBuilder.deadlinePolicyNavigation() {
    composable<DeadlinePolicy> {
        DeadlinePolicyScreen()
    }
}
