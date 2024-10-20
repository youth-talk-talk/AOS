package com.youthtalk

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.core.community.screen.community.CommunityScreen
import com.core.community.screen.detail.CommunityDetailScreen
import com.core.community.screen.write.CommunityWriteScreen
import com.core.home.HomeScreen
import com.core.mypage.MyPageScreen
import com.core.navigation.CommunityNavigation
import com.core.navigation.MainNav
import com.core.navigation.Nav
import com.example.policydetail.PolicyDetailScreen
import com.youth.search.SearchScreen
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.mainHomeActionBarColor
import com.youthtalk.model.Category
import com.youthtalk.specpolicy.SpecPolicyScreen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(goLogin: () -> Unit, checkPermission: (String) -> Boolean) {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val homeLazyListScrollState = rememberLazyListState()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (currentRoute == MainNav.Home.route) mainHomeActionBarColor.toArgb() else Color.Transparent.toArgb()
        }
    }
    Scaffold(
        bottomBar = {
            if (MainNav.isMainNavigation(currentRoute)) {
                BottomBar(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 6.dp)
                        .height(IntrinsicSize.Min)
                        .navigationBarsPadding(),
                    navHostController = navHostController,
                    currentRoute = currentRoute,
                    homeLazyListScrollState = homeLazyListScrollState,
                )
            }
        },
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
        startDestination = MainNav.Home.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        mainNavigation(navController, homeLazyListScrollState, goLogin = goLogin)

        composable(
            route = "${Nav.PolicyDetail.route}/{policyId}",
            arguments = listOf(
                navArgument("policyId") {
                    type = NavType.StringType
                },
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "http://youth-talk.com/detail_policy/{policyId}"
                    action = Intent.ACTION_VIEW
                },
            ),
        ) {
            it.arguments?.getString("policyId")?.let { policyId ->
                PolicyDetailScreen(
                    policyId = policyId,
                    onBack = { navController.popBackStack() },
                )
            }
        }

        composable(
            route = "${Nav.SpecPolicy.route}/{category}",
            arguments = listOf(
                navArgument("category") {
                    type = NavType.EnumType(Category::class.java)
                },
            ),
        ) {
            val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.arguments?.getSerializable("category", Category::class.java)
            } else {
                it.arguments?.getSerializable("category") as Category
            }
            category?.let {
                SpecPolicyScreen(
                    category = category,
                    onClickSearch = {
                        navController.navigate("${Nav.Search.route}/home")
                    },
                    onBack = { navController.popBackStack() },
                    onClickDetailPolicy = {
                        navController.navigate("${Nav.PolicyDetail.route}/$it") {
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                )
            }
        }

        composable(
            route = "${Nav.Search.route}/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                },
            ),
        ) {
            it.arguments?.getString("type")?.let { type ->
                SearchScreen(
                    type = type,
                    onClickDetailPolicy = { policyId ->
                        navController.navigate("${Nav.PolicyDetail.route}/$policyId")
                    },
                    onClickDetailPost = { postId ->
                        navController.navigate("${CommunityNavigation.CommunityDetail.route}/$postId")
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }
        }

        communityNavigation(navController, checkPermission = checkPermission)
    }
}

private fun NavGraphBuilder.communityNavigation(navController: NavHostController, checkPermission: (String) -> Boolean) {
    composable(
        route = "${CommunityNavigation.CommunityDetail.route}/{postId}",
        arguments = listOf(
            navArgument("postId") {
                type = NavType.LongType
            },
        ),
    ) {
        val postId = it.arguments?.getLong("postId") ?: -1
        CommunityDetailScreen(
            postId = postId,
            onBack = { navController.popBackStack() },
            goWriteScreen = { id, type -> navController.navigate("${CommunityNavigation.CommunityWrite.route}/$type/$id") },
        )
    }

    composable(
        route = "${CommunityNavigation.CommunityWrite.route}/{type}/{postId}",
        arguments = listOf(
            navArgument("type") {
                type = NavType.StringType
            },
            navArgument("postId") {
                type = NavType.LongType
            },
        ),
    ) {
        val type = it.arguments?.getString("type") ?: ""
        val id = it.arguments?.getLong("postId") ?: -1
        CommunityWriteScreen(
            type = type,
            postId = id,
            onBack = { navController.popBackStack() },
            checkPermission = checkPermission,
            goDetail = { postId ->
                navController.navigate("${CommunityNavigation.CommunityDetail.route}/$postId") {
                    popUpTo("${CommunityNavigation.CommunityWrite.route}/$type/$id") {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
        )
    }
}

private fun NavGraphBuilder.mainNavigation(navController: NavHostController, homeLazyListScrollState: LazyListState, goLogin: () -> Unit) {
    composable(route = MainNav.Home.route) {
        HomeScreen(
            homeLazyListScrollState = homeLazyListScrollState,
            onClickDetailPolicy = { policyId -> navController.navigate("${Nav.PolicyDetail.route}/$policyId") },
            onClickSearch = { navController.navigate("${Nav.Search.route}/home") },
            onClickSpecPolicy = { category ->
                navController.navigate("${Nav.SpecPolicy.route}/$category") {
                    restoreState = true
                    launchSingleTop = true
                }
            },
        )
    }

    composable(route = MainNav.Community.route) {
        CommunityScreen(
            onClickItem = { postId ->
                navController.navigate("${CommunityNavigation.CommunityDetail.route}/$postId") {
                    restoreState = true
                    launchSingleTop = true
                }
            },
            writePost = { type ->
                navController.navigate("${CommunityNavigation.CommunityWrite.route}/$type/${-1}") {
                    restoreState = true
                    launchSingleTop = true
                }
            },
            onClickSearch = { type ->
                navController.navigate("${Nav.Search.route}/$type") {
                    restoreState = true
                    launchSingleTop = true
                }
            },
        )
    }

    composable(route = MainNav.MyPage.route) {
        MyPageScreen(
            onClickDetailPolicy = { navController.navigate("${Nav.PolicyDetail.route}/$it") },
            goLogin = goLogin,
            policyDetail = { navController.navigate("${Nav.PolicyDetail.route}/$it") },
            postDetail = { navController.navigate("${CommunityNavigation.CommunityDetail.route}/$it") },
        )
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier, homeLazyListScrollState: LazyListState, navHostController: NavHostController, currentRoute: String?) {
    val bottomNavigation =
        listOf(
            MainNav.Community,
            MainNav.Home,
            MainNav.MyPage,
        )

    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        bottomNavigation.forEach { mainNav ->
            val color = if (mainNav.route == MainNav.includeMainNav(currentRoute)) Color.Black else Color.Gray
            BottomIcon(
                navHostController = navHostController,
                mainNav = mainNav,
                color = color,
                scrollTop = {
                    if (currentRoute == MainNav.Home.route) {
                        scope.launch {
                            homeLazyListScrollState.scrollToItem(0)
                        }
                    }
                },
            )
        }
    }
}

@Composable
fun RowScope.BottomIcon(navHostController: NavHostController, mainNav: MainNav, color: Color, scrollTop: () -> Unit) {
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .weight(1f)
            .align(Alignment.CenterVertically)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
            ) {
                if (navHostController.currentDestination?.route != mainNav.route) {
                    navHostController.navigate(mainNav.route) {
                        popUpTo(navHostController.graph.id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                } else {
                    scrollTop()
                }
            },
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Box(
                modifier =
                Modifier
                    .size(24.dp),
            ) {
                Icon(
                    modifier =
                    Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    painter = painterResource(id = mainNav.icon),
                    contentDescription = mainNav.title,
                    tint = color,
                )
            }
            Text(
                text = mainNav.title,
                style =
                MaterialTheme.typography.bodySmall
                    .copy(color = color, fontWeight = FontWeight.Bold),
            )
        }
    }
}

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
