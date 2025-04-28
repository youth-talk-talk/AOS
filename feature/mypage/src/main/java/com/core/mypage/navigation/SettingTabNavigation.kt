package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.core.mypage.screen.SettingAccount
import com.core.mypage.screen.SettingScreen
import com.core.navigation.navigator.Account
import com.core.navigation.navigator.HomeTabNavigation

fun NavController.navigateSetting(navOptions: NavOptions? = null) {
    navigate(HomeTabNavigation.Setting, navOptions)
}

fun NavController.navigateAccount(navOptions: NavOptions? = null) {
    navigate(Account, navOptions)
}

fun NavGraphBuilder.settingTabNavigation(
    onClickProfileCard: () -> Unit,
    onClickEtc: () -> Unit,
    onClickTerms: () -> Unit,
    onClickSettingScrap: () -> Unit,
) {
    navigation(
        startDestination = "Setting",
        route = "Setting_graph",
    ) {
        composable<HomeTabNavigation.Setting> {
            SettingScreen(
                onClickProfileCard = onClickProfileCard,
                onClickEtc = onClickEtc,
                onClickTerms = onClickTerms,
                onClickSettingScrap = onClickSettingScrap,
            )
        }

        composable<Account> {
            SettingAccount()
        }
    }
}
