package com.core.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.core.login.LoginViewModel
import com.core.navigation.LoginRouteName

@Composable
fun LoginNavHostScreen(modifier: Modifier = Modifier, viewModel: LoginViewModel) {
    val navHostController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = LoginRouteName.LOGIN_SCREEN,
    ) {
        composable(LoginRouteName.LOGIN_SCREEN) {
            LoginScreen(
                viewModel,
                goAgreeScreen = {
                    navHostController.navigate(LoginRouteName.AGREE_SCREEN) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(LoginRouteName.AGREE_SCREEN) {
            AgreeScreen(
                clickNext = {
                    navHostController.navigate(LoginRouteName.INFORMATION_SCREEN) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBack = { navHostController.popBackStack() },
            )
        }
        composable(LoginRouteName.INFORMATION_SCREEN) {
            InformationScreen(viewModel, onBack = { navHostController.popBackStack() })
        }
    }
}
