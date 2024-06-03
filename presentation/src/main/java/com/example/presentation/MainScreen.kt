package com.example.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.presentation.ui.main.CommunityScreen
import com.example.presentation.ui.main.HomeScreen
import com.example.presentation.ui.main.MyPageScreen
import com.example.presentation.ui.theme.YongProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomBar(
                navHostController = navHostController,
                currentRoute = currentRoute,
                modifier = Modifier.height(56.dp),
            )
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                NavHostScreen(navController = navHostController)
            }
        },
    )
}

@Composable
fun NavHostScreen(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MainNav.Home.route) {
        composable(route = MainNav.Home.route) {
            HomeScreen()
        }

        composable(route = MainNav.Community.route) {
            CommunityScreen()
        }

        composable(route = MainNav.MyPage.route) {
            MyPageScreen()
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun BottomBar(
    navHostController: NavHostController,
    currentRoute: String?,
    modifier: Modifier = Modifier,
) {
    val bottomNavigation =
        listOf(
            MainNav.Community,
            MainNav.Home,
            MainNav.MyPage,
        )

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
            )
        }
    }
}

@Composable
fun RowScope.BottomIcon(
    navHostController: NavHostController,
    mainNav: MainNav,
    color: Color,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .weight(1f)
                .align(Alignment.CenterVertically)
                .clickable {
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

@Preview
@Composable
private fun MainScreenPreview() {
    YongProjectTheme {
        MainScreen()
    }
}
