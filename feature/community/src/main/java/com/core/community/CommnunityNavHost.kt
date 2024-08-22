package com.core.community

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.core.community.screen.CommunityDetailScreen
import com.core.community.screen.CommunityScreen
import com.core.community.screen.CommunityWriteScreen
import com.core.navigation.CommunityNavigation

@Composable
fun CommunityNavHost() {
    val navHost = rememberNavController()

    NavHost(
        navController = navHost,
        startDestination = CommunityNavigation.Community.route,
    ) {
        composable(
            route = CommunityNavigation.Community.route,
        ) {
            CommunityScreen(
                onClickItem = {
                    navHost.navigate(CommunityNavigation.CommunityDetail.route) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(
            route = "${CommunityNavigation.CommunityDetail.route}/{type}/{postId}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                },
                navArgument("postId") {
                    type = NavType.LongType
                },
            ),
        ) {
            val postId = it.arguments?.getLong("postId") ?: -1
            val type = it.arguments?.getString("type") ?: ""
            CommunityDetailScreen(
                postId = postId,
                type = type,
            )
        }

        composable(
            route = CommunityNavigation.CommunityWrite.route,
        ) {
            CommunityWriteScreen()
        }
    }
}
