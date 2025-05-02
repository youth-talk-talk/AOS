package com.core.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.home.screen.Home
import com.core.navigation.model.CommentType
import com.core.navigation.model.ScrapPostType
import com.core.navigation.navigator.Navigation

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(Navigation.Main, navOptions)
}

fun NavGraphBuilder.homeNavigation(
    onClickEtc: () -> Unit,
    onClickTerms: () -> Unit,
    onClickSettingScrap: () -> Unit,
    onClickSettingPost: (ScrapPostType) -> Unit,
    onClickSettingComment: (CommentType) -> Unit,
    onClickSettingNotification: () -> Unit,
    onClickPolicySearch: () -> Unit,
    onClickPopularPolicy: () -> Unit,
    onClickNewPolicy: () -> Unit,
    onClickRecentViewPolicy: () -> Unit,
    goLogin: () -> Unit,
) {
    composable<Navigation.Main> {
        Home(
            onClickPolicySearch = onClickPolicySearch,
            onClickEtc = onClickEtc,
            onClickTerms = onClickTerms,
            onClickSettingScrap = onClickSettingScrap,
            onClickSettingPost = onClickSettingPost,
            onClickSettingComment = onClickSettingComment,
            onClickSettingNotification = onClickSettingNotification,
            onClickPopularPolicy = onClickPopularPolicy,
            onClickNewPolicy = onClickNewPolicy,
            onClickRecentViewPolicy = onClickRecentViewPolicy,
            goLogin = goLogin,
        )
    }
}
