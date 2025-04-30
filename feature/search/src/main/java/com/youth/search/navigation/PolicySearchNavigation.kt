package com.youth.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.navigation.navigator.PolicySearch
import com.youth.search.screen.PolicySearchScreen

fun NavController.navigatePolicySearch(navOptions: NavOptions? = null) {
    navigate(PolicySearch, navOptions)
}

fun NavGraphBuilder.policySearchNavigation(onBack: () -> Unit) {
    composable<PolicySearch> {
        PolicySearchScreen(
            onBack = onBack,
        )
    }
}
