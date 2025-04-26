package com.core.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.home.screen.Home
import com.core.navigation.navigator.Navigation

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(Navigation.Main, navOptions)
}

fun NavGraphBuilder.homeNavigation(onClickEtc: () -> Unit) {
    composable<Navigation.Main> {
        Home(
            onClickEtc = onClickEtc,
        )
    }
}
