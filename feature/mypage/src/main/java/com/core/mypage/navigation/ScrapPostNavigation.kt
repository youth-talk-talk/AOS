package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.mypage.screen.ScrapPostScreen
import com.core.navigation.model.ScrapPostType
import com.core.navigation.navigator.ScrapPost

fun NavController.navigateSettingScrapPost(type: ScrapPostType, navOptions: NavOptions? = null) {
    navigate(ScrapPost(type), navOptions)
}

fun NavGraphBuilder.settingScrapPostNavigation(onClickPostDetail: (Long) -> Unit, onBack: () -> Unit) {
    composable<ScrapPost> {
        ScrapPostScreen(
            onClickPostDetail = onClickPostDetail,
            onBack = onBack
        )
    }
}
