package com.feature.policy.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.navigation.navigator.PolicyOverView
import com.feature.policy.screen.PolicyOverviewScreen

fun NavController.navigatePolicyOverView(navOptions: NavOptions? = null) {
    navigate(PolicyOverView, navOptions)
}

fun NavGraphBuilder.policyOverViewNavigation() {
    composable<PolicyOverView> {
        PolicyOverviewScreen()
    }
}
