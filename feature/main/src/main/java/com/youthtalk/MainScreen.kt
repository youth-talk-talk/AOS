package com.youthtalk

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.core.community.navigation.communityDetailNavigation
import com.core.community.navigation.communityWriteNavigation
import com.core.community.navigation.navigateCommunity
import com.core.community.navigation.navigateCommunityDetail
import com.core.community.navigation.navigateCommunityWrite
import com.core.home.navigation.homeNavigation
import com.core.home.navigation.navigateNewPolicy
import com.core.home.navigation.navigatePopularPolicy
import com.core.home.navigation.newPolicyNavigation
import com.core.home.navigation.popularPolicyNavigation
import com.core.mypage.navigation.navigateSettingComment
import com.core.mypage.navigation.navigateSettingEtc
import com.core.mypage.navigation.navigateSettingNotification
import com.core.mypage.navigation.navigateSettingScrapPolicy
import com.core.mypage.navigation.navigateSettingScrapPost
import com.core.mypage.navigation.navigateSettingTerms
import com.core.mypage.navigation.settingCommentNavigation
import com.core.mypage.navigation.settingEtcNavigation
import com.core.mypage.navigation.settingNotificationNavigation
import com.core.mypage.navigation.settingScrapPolicyNavigation
import com.core.mypage.navigation.settingScrapPostNavigation
import com.core.mypage.navigation.settingTermsNavigation
import com.core.navigation.navigator.CommunityDetail
import com.core.navigation.navigator.Navigation
import com.feature.policy.navigation.deadlinePolicyNavigation
import com.feature.policy.navigation.navigateDeadlinePolicy
import com.feature.policy.navigation.navigateRecentlyViewPolicy
import com.feature.policy.navigation.recentlyViewPolicyNavigation
import com.feature.policydetail.navigation.navigatePolicyDetail
import com.feature.policydetail.navigation.policyDetailNavigation
import com.youth.search.navigation.communitySearchNavigation
import com.youth.search.navigation.navigateCommunitySearch
import com.youth.search.navigation.navigatePolicySearch
import com.youth.search.navigation.policySearchNavigation
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    goLogin: () -> Unit,
    checkPermission: (String) -> Boolean,
    showSnackBar: (String) -> Unit
) {
    val homeNavHostController = rememberNavController()

    Column(modifier = modifier) {
        NavHostScreen(
            navController = navController,
            homeNavController = homeNavHostController,
            goLogin = goLogin,
            checkPermission = checkPermission,
            showSnackBar = showSnackBar
        )
    }
}

@Composable
fun NavHostScreen(
    navController: NavHostController,
    homeNavController: NavHostController,
    goLogin: () -> Unit,
    checkPermission: (String) -> Boolean,
    showSnackBar: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Navigation.Main,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        homeNavigation(
            homeNavController = homeNavController,
            onClickPopularPolicy = navController::navigatePopularPolicy,
            onClickPolicySearch = navController::navigatePolicySearch,
            onClickEtc = navController::navigateSettingEtc,
            onClickTerms = navController::navigateSettingTerms,
            onClickSettingScrap = navController::navigateSettingScrapPolicy,
            onClickSettingPost = navController::navigateSettingScrapPost,
            onClickSettingComment = navController::navigateSettingComment,
            onClickSettingNotification = navController::navigateSettingNotification,
            onClickNewPolicy = navController::navigateNewPolicy,
            onClickRecentViewPolicy = navController::navigateRecentlyViewPolicy,
            onClickDeadlinePolicy = navController::navigateDeadlinePolicy,
            onClickCommunitySearch = navController::navigateCommunitySearch,
            onClickPostDetail = navController::navigateCommunityDetail,
            onClickPolicyDetail = navController::navigatePolicyDetail,
            onClickCommunityWrite = navController::navigateCommunityWrite,
            goLogin = goLogin,
            checkPermission = checkPermission
        )
        settingEtcNavigation(
            onBack = navController::popBackStack,
            goLogin = goLogin
        )
        settingTermsNavigation(
            onBack = navController::popBackStack
        )
        settingScrapPolicyNavigation(
            onBack = navController::popBackStack,
            onClickPolicyDetail = navController::navigatePolicyDetail
        )
        settingScrapPostNavigation(
            onClickPostDetail = navController::navigateCommunityDetail,
            onBack = navController::popBackStack
        )
        settingCommentNavigation(
            onBack = navController::popBackStack,
            showSnackBar = showSnackBar,
            onClickPostDetail = navController::navigateCommunityDetail,
            onClickPolicyDetail = navController::navigatePolicyDetail
        )
        settingNotificationNavigation(
            onBack = navController::popBackStack
        )

        policySearchNavigation(
            onBack = { navController.popBackStack() },
            onClickPolicyDetail = navController::navigatePolicyDetail
        )
        communitySearchNavigation(
            onBack = { navController.popBackStack() },
            onClickPostDetail = navController::navigateCommunityDetail
        )
        popularPolicyNavigation(
            onBack = navController::popBackStack,
            onClickPolicyDetail = navController::navigatePolicyDetail
        )
        newPolicyNavigation(
            onBack = navController::popBackStack,
            onClickPolicyDetail = navController::navigatePolicyDetail
        )
        recentlyViewPolicyNavigation(
            onBack = navController::popBackStack,
            onClickPolicyDetail = navController::navigatePolicyDetail
        )
        deadlinePolicyNavigation(
            onBack = navController::popBackStack,
            onClickPolicyDetail = navController::navigatePolicyDetail
        )

        communityDetailNavigation(
            showSnackBar = showSnackBar,
            onBack = navController::popBackStack,
            onModifyWriteCommunity = { postType, postId ->
                navController.navigateCommunityWrite(postType, postId)
            }
        )
        communityWriteNavigation(
            checkPermission = checkPermission,
            onBack = { navController.popBackStack() },
            onCreate = {
                homeNavController.navigateCommunity(postType = it) {
                    popUpTo(homeNavController.graph.id) {
                        saveState = true
                        inclusive = true
                    }
                }
                navController.popBackStack()
            },
            onModify = { postId ->
                navController.navigateCommunityDetail(postId) {
                    popUpTo<CommunityDetail> {
                        inclusive = true
                    }
                }
            }
        )

        policyDetailNavigation(
            onBack = navController::popBackStack,
            showSnackBar = showSnackBar
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    YongProjectTheme {
        MainScreen(
            navController = rememberNavController(),
            goLogin = {},
            checkPermission = { false },
            showSnackBar = {}
        )
    }
}
