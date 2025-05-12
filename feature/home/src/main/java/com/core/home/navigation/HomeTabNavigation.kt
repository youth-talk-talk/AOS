package com.core.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.home.screen.HomeScreen
import com.core.navigation.navigator.HomeTabNavigation
import com.youthtalk.model.Category

fun NavController.navigateHomeTab(navOptions: NavOptions) {
    navigate(HomeTabNavigation.Home, navOptions)
}

fun NavGraphBuilder.homeTabNavigation(
    onClickPolicySearch: () -> Unit,
    onClickPopularPolicy: () -> Unit,
    onClickNewPolicy: () -> Unit,
    onClickPolicyDetail: (Long) -> Unit,
    onClickPostDetail: (Long) -> Unit,
    onClickPolicyOverView: (Category) -> Unit
) {
    composable<HomeTabNavigation.Home> {
        HomeScreen(
            onClickPolicySearch = onClickPolicySearch,
            onClickPopularPolicy = onClickPopularPolicy,
            onClickNewPolicy = onClickNewPolicy,
            onClickPolicyDetail = onClickPolicyDetail,
            onClickPolicyOverView = onClickPolicyOverView,
            onClickPostDetail = onClickPostDetail
        )
    }
}
