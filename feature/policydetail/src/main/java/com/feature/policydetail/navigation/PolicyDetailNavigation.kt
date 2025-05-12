package com.feature.policydetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.navigation.navigator.PolicyDetail
import com.feature.policydetail.screen.PolicyDetailScreen

fun NavController.navigatePolicyDetail(policyId: Long, navOptions: NavOptions? = null) {
    navigate(PolicyDetail(policyId), navOptions)
}

fun NavGraphBuilder.policyDetailNavigation() {
    composable<PolicyDetail> {
        PolicyDetailScreen()
    }
}
