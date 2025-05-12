package com.core.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.home.screen.PopularPolicyScreen
import com.core.navigation.navigator.NewPolicy

fun NavController.navigatePopularPolicy(navOptions: NavOptions? = null) {
    navigate(NewPolicy, navOptions)
}

fun NavGraphBuilder.popularPolicyNavigation(onBack: () -> Unit) {
    composable<NewPolicy> {
        PopularPolicyScreen(
            onBack = onBack
        )
    }
}
