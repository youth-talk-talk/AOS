package com.feature.policydetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.core.navigation.navigator.PolicyDetail
import com.feature.policydetail.screen.PolicyDetailScreenRoot

fun NavController.navigatePolicyDetail(policyId: Long, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(PolicyDetail(policyId), navOptions)
}

fun NavGraphBuilder.policyDetailNavigation(onBack: () -> Unit, showSnackBar: (String) -> Unit) {
    composable<PolicyDetail> {
        PolicyDetailScreenRoot(
            onBack = onBack,
            showSnackBar = showSnackBar
        )
    }
}
