package com.youthtalk

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.core.home.navigation.homeNavigation
import com.core.mypage.navigation.navigateSettingEtc
import com.core.mypage.navigation.navigateSettingScrapPolicy
import com.core.mypage.navigation.navigateSettingTerms
import com.core.mypage.navigation.settingEtcNavigation
import com.core.mypage.navigation.settingScrapPolicyNavigation
import com.core.mypage.navigation.settingTermsNavigation
import com.core.navigation.navigator.Navigation
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun MainScreen(goLogin: () -> Unit, checkPermission: (String) -> Boolean) {
    val navHostController = rememberNavController()
    val homeLazyListScrollState = rememberLazyListState()
    Scaffold(
        content = {
            Column(modifier = Modifier.padding(it)) {
                NavHostScreen(
                    navController = navHostController,
                    homeLazyListScrollState = homeLazyListScrollState,
                    goLogin = goLogin,
                    checkPermission = checkPermission,
                )
            }
        },
    )
}

@Composable
fun NavHostScreen(
    navController: NavHostController,
    homeLazyListScrollState: LazyListState,
    goLogin: () -> Unit,
    checkPermission: (String) -> Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = Navigation.Main,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        homeNavigation(
            onClickEtc = navController::navigateSettingEtc,
            onClickTerms = navController::navigateSettingTerms,
            onClickSettingScrap = navController::navigateSettingScrapPolicy,
        )
        settingEtcNavigation()
        settingTermsNavigation()
        settingScrapPolicyNavigation()

//        mainNavigation(navController, homeLazyListScrollState, goLogin = goLogin)
//
//        composable(
//            route = "${Nav.PolicyDetail.route}/{policyId}",
//            arguments = listOf(
//                navArgument("policyId") {
//                    type = NavType.StringType
//                },
//            ),
//            deepLinks = listOf(
//                navDeepLink {
//                    uriPattern = "http://youth-talk.com/detail_policy/{policyId}"
//                    action = Intent.ACTION_VIEW
//                },
//            ),
//        ) {
//            it.arguments?.getString("policyId")?.let { policyId ->
//                PolicyDetailScreen(
//                    policyId = policyId,
//                    onBack = { navController.popBackStack() },
//                )
//            }
//        }
//
//        composable(
//            route = "${Nav.SpecPolicy.route}/{category}",
//            arguments = listOf(
//                navArgument("category") {
//                    type = NavType.EnumType(Category::class.java)
//                },
//            ),
//        ) {
//            val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                it.arguments?.getSerializable("category", Category::class.java)
//            } else {
//                it.arguments?.getSerializable("category") as Category
//            }
//            category?.let {
//                SpecPolicyScreen(
//                    category = category,
//                    onClickSearch = {
//                        navController.navigate("${Nav.Search.route}/home")
//                    },
//                    onBack = { navController.popBackStack() },
//                    onClickDetailPolicy = {
//                        navController.navigate("${Nav.PolicyDetail.route}/$it") {
//                            restoreState = true
//                            launchSingleTop = true
//                        }
//                    },
//                )
//            }
//        }
//
//        composable(
//            route = "${Nav.Search.route}/{type}",
//            arguments = listOf(
//                navArgument("type") {
//                    type = NavType.StringType
//                },
//            ),
//        ) {
//            it.arguments?.getString("type")?.let { type ->
//                SearchScreen(
//                    type = type,
//                    onClickDetailPolicy = { policyId ->
//                        navController.navigate("${Nav.PolicyDetail.route}/$policyId")
//                    },
//                    onClickDetailPost = { postId ->
//                        navController.navigate("${CommunityNavigation.CommunityDetail.route}/$postId")
//                    },
//                    onBack = {
//                        navController.popBackStack()
//                    },
//                )
//            }
//        }
//
//        communityNavigation(navController, checkPermission = checkPermission)
    }
}

// private fun NavGraphBuilder.communityNavigation(navController: NavHostController, checkPermission: (String) -> Boolean) {
//    composable(
//        route = "${CommunityNavigation.CommunityDetail.route}/{postId}",
//        arguments = listOf(
//            navArgument("postId") {
//                type = NavType.LongType
//            },
//        ),
//        enterTransition = { slideInVertically(initialOffsetY = { it / 2 }) + fadeIn() },
//        exitTransition = { slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut() },
//    ) {
//        val postId = it.arguments?.getLong("postId") ?: -1
//        CommunityDetailScreen(
//            postId = postId,
//            onBack = {
//                navController.popBackStack()
//            },
//            goWriteScreen = { id, type -> navController.navigate("${CommunityNavigation.CommunityWrite.route}/$type/$id") },
//        )
//    }
//
//    composable(
//        route = "${CommunityNavigation.CommunityWrite.route}/{type}/{postId}",
//        arguments = listOf(
//            navArgument("type") {
//                type = NavType.StringType
//            },
//            navArgument("postId") {
//                type = NavType.LongType
//            },
//        ),
//    ) {
//        val type = it.arguments?.getString("type") ?: ""
//        val id = it.arguments?.getLong("postId") ?: -1
//        CommunityWriteScreen(
//            type = type,
//            postId = id,
//            onBack = { navController.popBackStack() },
//            checkPermission = checkPermission,
//            goDetail = { postId ->
//                navController.navigate("${CommunityNavigation.CommunityDetail.route}/$postId") {
//                    val route = if (id == -1L) {
//                        "${navController.currentDestination?.route}"
//                    } else {
//                        "${CommunityNavigation.CommunityDetail.route}/$postId"
//                    }
//                    popUpTo(route) {
//                        inclusive = true
//                    }
//                    launchSingleTop = true
//                }
//            },
//        )
//    }
// }
//
// private fun NavGraphBuilder.mainNavigation(navController: NavHostController, homeLazyListScrollState: LazyListState, goLogin: () -> Unit) {
//    composable(route = MainNav.Home.route) {
//        HomeScreen(
//            homeLazyListScrollState = homeLazyListScrollState,
//            onClickDetailPolicy = { policyId -> navController.navigate("${Nav.PolicyDetail.route}/$policyId") },
//            onClickSearch = { navController.navigate("${Nav.Search.route}/home") },
//            onClickSpecPolicy = { category ->
//                navController.navigate("${Nav.SpecPolicy.route}/$category") {
//                    restoreState = true
//                    launchSingleTop = true
//                }
//            },
//        )
//    }
//
//    composable(route = MainNav.Community.route) {
//        CommunityScreen(
//            onClickItem = { postId ->
//                navController.navigate("${CommunityNavigation.CommunityDetail.route}/$postId") {
//                    restoreState = true
//                    launchSingleTop = true
//                }
//            },
//            writePost = { type ->
//                navController.navigate("${CommunityNavigation.CommunityWrite.route}/$type/${-1}") {
//                    restoreState = true
//                    launchSingleTop = true
//                }
//            },
//            onClickSearch = { type ->
//                navController.navigate("${Nav.Search.route}/$type") {
//                    restoreState = true
//                    launchSingleTop = true
//                }
//            },
//        )
//    }
//
//    composable(route = MainNav.MyPage.route) {
//        MyPageScreen(
//            onClickDetailPolicy = { navController.navigate("${Nav.PolicyDetail.route}/$it") },
//            goLogin = goLogin,
//            policyDetail = { navController.navigate("${Nav.PolicyDetail.route}/$it") },
//            postDetail = { navController.navigate("${CommunityNavigation.CommunityDetail.route}/$it") },
//        )
//    }
// }

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    YongProjectTheme {
        MainScreen(
            goLogin = {},
            checkPermission = { false },
        )
    }
}
