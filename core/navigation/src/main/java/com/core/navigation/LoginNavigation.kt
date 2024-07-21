package com.core.navigation

sealed class LoginNavigation(
    private val route: String,
) {
    data object Login : LoginNavigation(LoginRouteName.LOGIN_SCREEN)
    data object Agree : LoginNavigation(LoginRouteName.AGREE_SCREEN)
    data object Information : LoginNavigation(LoginRouteName.INFORMATION_SCREEN)
}

object LoginRouteName {
    const val LOGIN_SCREEN = "login"
    const val AGREE_SCREEN = "agree"
    const val INFORMATION_SCREEN = "infromation"
}
