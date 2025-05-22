package com.feature.policy.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.navigation.navigator.PolicyOverView
import com.feature.policy.screen.PolicyOverviewScreen
import com.youthtalk.model.typeenum.Category

fun NavController.navigatePolicyOverView(category: Category, navOptions: NavOptions? = null) {
    navigate(PolicyOverView(category), navOptions)
}

fun NavGraphBuilder.policyOverViewNavigation(onBack: () -> Unit, onClickPolicyDetail: (Long) -> Unit, onClickPolicySearch: () -> Unit) {
    composable<PolicyOverView> {
        PolicyOverviewScreen(
            onBack = onBack,
            onClickPolicyDetail = onClickPolicyDetail,
            onClickPolicySearch = onClickPolicySearch
        )
    }
}
