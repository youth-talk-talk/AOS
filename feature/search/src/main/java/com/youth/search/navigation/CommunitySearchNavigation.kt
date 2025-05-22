package com.youth.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.navigation.navigator.CommunitySearch
import com.youth.search.screen.CommunitySearchScreen
import com.youthtalk.model.post.PostSubject

fun NavController.navigateCommunitySearch(communityType: PostSubject, navOptions: NavOptions? = null) {
    navigate(CommunitySearch(communityType), navOptions)
}

fun NavGraphBuilder.communitySearchNavigation(onBack: () -> Unit, onClickPostDetail: (Long) -> Unit) {
    composable<CommunitySearch> {
        CommunitySearchScreen(
            onBack = onBack,
            onClickPostDetail = onClickPostDetail
        )
    }
}
