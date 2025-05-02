package com.core.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.home.screen.NewPolicyScreen
import com.core.navigation.navigator.PopularPolicy

fun NavController.navigateNewPolicy(navOptions: NavOptions? = null) {
    navigate(PopularPolicy, navOptions)
}

fun NavGraphBuilder.newPolicyNavigation(onBack: () -> Unit) {
    composable<PopularPolicy> {
        NewPolicyScreen(
            onBack = onBack,
        )
    }
}
