package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.mypage.screen.NotificationScreen
import com.core.navigation.navigator.Notification

fun NavController.navigateSettingNotification(navOptions: NavOptions? = null) {
    navigate(Notification, navOptions)
}

fun NavGraphBuilder.settingNotificationNavigation(onBack: () -> Unit) {
    composable<Notification> {
        NotificationScreen(
            onBack = onBack
        )
    }
}
