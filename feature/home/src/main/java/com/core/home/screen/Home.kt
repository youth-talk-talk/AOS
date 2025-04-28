package com.core.home.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.core.community.navigation.communityNavigation
import com.core.home.navigation.BottomNavigation
import com.core.home.navigation.homeTabNavigation
import com.core.mypage.navigation.navigateAccount
import com.core.mypage.navigation.settingTabNavigation
import com.core.navigation.model.CommentType
import com.core.navigation.model.ScrapPostType
import com.core.navigation.navigator.HomeTabNavigation
import com.feature.policy.navigation.policyTabNavigation

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onClickEtc: () -> Unit,
    onClickTerms: () -> Unit,
    onClickSettingScrap: () -> Unit,
    onClickSettingPost: (ScrapPostType) -> Unit,
    onClickSettingComment: (CommentType) -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "Home"
    Scaffold(
        bottomBar = {
            BottomNavigation(
                route = currentRoute.toHomeTabNavigation(),
                onClickNavigation = {
                    navController.navigate(it) {
                        popUpTo(navController.graph.id) {
                            saveState = true
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController,
            startDestination = HomeTabNavigation.Home,
        ) {
            homeTabNavigation()
            communityNavigation()
            policyTabNavigation()
            settingTabNavigation(
                onClickProfileCard = navController::navigateAccount,
                onClickEtc = onClickEtc,
                onClickTerms = onClickTerms,
                onClickSettingScrap = onClickSettingScrap,
                onClickSettingPost = onClickSettingPost,
                onClickSettingComment = onClickSettingComment,
            )
        }
    }
}

private fun String.toHomeTabNavigation(): HomeTabNavigation = when (this) {
    "Home" -> HomeTabNavigation.Home
    "Setting", "Account" -> HomeTabNavigation.Setting
    "Community" -> HomeTabNavigation.Community
    "Policy" -> HomeTabNavigation.Policy
    else -> HomeTabNavigation.Home
}
