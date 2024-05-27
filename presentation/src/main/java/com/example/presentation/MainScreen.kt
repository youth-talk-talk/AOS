package com.example.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun MainScreen() {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomBar(navHostController = navHostController, currentRoute = currentRoute)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                NavHostScreen(navController = navHostController)
            }
        },
    )
}

@Composable
fun NavHostScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
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

@Composable
fun BottomBar(
    navHostController: NavHostController,
    currentRoute: String?,
) {
    val bottomNavigation =
        listOf(
            MainNav.Community,
            MainNav.Home,
            MainNav.MyPage,
        )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        bottomNavigation.forEachIndexed { _, mainNav ->
            NavigationBarItem(
                selected = currentRoute == mainNav.route,
                onClick = {
                    navHostController.navigate(mainNav.route) {
                        popUpTo(MainNav.Home.route)
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = mainNav.icon),
                        contentDescription = mainNav.title,
                        modifier = Modifier.size(24.dp),
                    )
                },
                label = {
                    Text(
                        text = mainNav.title,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    )
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.background,
                        selectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                    ),
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
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
