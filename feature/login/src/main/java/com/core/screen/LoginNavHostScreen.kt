package com.core.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.core.login.LoginViewModel
import com.core.navigation.LoginRouteName

@Composable
fun LoginNavHostScreen(viewModel: LoginViewModel) {
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = LoginRouteName.LOGIN_SCREEN) {
        composable(LoginRouteName.LOGIN_SCREEN) {
            LoginScreen(navHostController, viewModel)
        }
        composable(LoginRouteName.AGREE_SCREEN) {
            AgreeScreen(
                clickCancel = {
                    navHostController.navigate(LoginRouteName.LOGIN_SCREEN) {
                        popUpTo(LoginRouteName.AGREE_SCREEN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                clickNext = {
                    navHostController.navigate(LoginRouteName.INFORMATION_SCREEN) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
        composable(LoginRouteName.INFORMATION_SCREEN) {
            InformationScreen(viewModel)
        }
    }
}
