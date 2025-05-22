package com.core.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.home.screen.PopularPolicyScreen
import com.core.navigation.navigator.PopularPolicy

fun NavController.navigatePopularPolicy(policies: String, navOptions: NavOptions? = null) {
    navigate(PopularPolicy(policies), navOptions)
}

fun NavGraphBuilder.popularPolicyNavigation(onBack: () -> Unit, onClickPolicyDetail: (Long) -> Unit) {
    composable<PopularPolicy> {
        PopularPolicyScreen(
            onBack = onBack,
            onClickPolicyDetail = onClickPolicyDetail
        )
    }
}
