package com.youthtalk

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.core.community.CommunityScreen
import com.core.home.HomeScreen
import com.core.mypage.MyPageScreen
import com.core.navigation.MainNav
import com.youthtalk.designsystem.YongProjectTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val homeLazyListScrollState = rememberLazyListState()

    Scaffold(
        bottomBar = {
            BottomBar(
                modifier = Modifier.height(56.dp),
                navHostController = navHostController,
                currentRoute = currentRoute,
                homeLazyListScrollState = homeLazyListScrollState,
            )
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                NavHostScreen(
                    navController = navHostController,
                    homeLazyListScrollState = homeLazyListScrollState,
                )
            }
        },
    )
}

@Composable
fun NavHostScreen(navController: NavHostController, homeLazyListScrollState: LazyListState) {
    NavHost(navController = navController, startDestination = MainNav.Home.route) {
        composable(route = MainNav.Home.route) {
            HomeScreen(
                homeLazyListScrollState = homeLazyListScrollState,
            )
        }

        composable(route = MainNav.Community.route) {
            CommunityScreen()
        }

        composable(route = MainNav.MyPage.route) {
            MyPageScreen(listOf())
        }
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
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        bottomNavigation.forEach { mainNav ->
            val color = if (mainNav.route == currentRoute) Color.Black else Color.Gray
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
                scrollTop()
                navHostController.navigate(mainNav.route) {
                    popUpTo(MainNav.Home.route)
                    launchSingleTop = true
                    restoreState = true
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
        MainScreen()
    }
}
