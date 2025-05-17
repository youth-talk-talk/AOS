package com.core.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.home.screen.Home
import com.core.navigation.model.CommentType
import com.core.navigation.model.ScrapPostType
import com.core.navigation.navigator.Navigation
import com.youthtalk.model.post.PostSubject

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(Navigation.Main, navOptions)
}

fun NavGraphBuilder.homeNavigation(
    homeNavController: NavHostController,
    onClickEtc: () -> Unit,
    onClickTerms: () -> Unit,
    onClickSettingScrap: () -> Unit,
    onClickSettingPost: (ScrapPostType) -> Unit,
    onClickSettingComment: (CommentType) -> Unit,
    onClickSettingNotification: () -> Unit,
    onClickPolicySearch: () -> Unit,
    onClickPopularPolicy: (String) -> Unit,
    onClickNewPolicy: () -> Unit,
    onClickRecentViewPolicy: () -> Unit,
    onClickDeadlinePolicy: () -> Unit,
    onClickCommunitySearch: (PostSubject) -> Unit,
    onClickPostDetail: (Long) -> Unit,
    onClickPolicyDetail: (Long) -> Unit,
    onClickCommunityWrite: (PostSubject) -> Unit,
    goLogin: () -> Unit
) {
    composable<Navigation.Main> {
        Home(
            navController = homeNavController,
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
            onClickDeadlinePolicy = onClickDeadlinePolicy,
            onClickCommunitySearch = onClickCommunitySearch,
            onClickPostDetail = onClickPostDetail,
            onClickPolicyDetail = onClickPolicyDetail,
            onClickCommunityWrite = onClickCommunityWrite,
            goLogin = goLogin
        )
    }
}
