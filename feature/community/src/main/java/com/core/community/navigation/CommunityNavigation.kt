package com.core.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.core.community.screen.community.CommunityScreen
import com.core.navigation.navigator.HomeTabNavigation
import com.youthtalk.model.post.PostSubject

fun NavController.navigateCommunity(postType: PostSubject = PostSubject.REVIEW, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(HomeTabNavigation.Community(postType), navOptions)
}

fun NavGraphBuilder.communityNavigation(
    onClickCommunitySearch: (PostSubject) -> Unit,
    onClickPostDetail: (Long) -> Unit,
    onClickCommunityWrite: (PostSubject) -> Unit
) {
    composable<HomeTabNavigation.Community> {
        val data = it.toRoute<HomeTabNavigation.Community>()
        CommunityScreen(
            postType = data.postType,
            onClickCommunitySearch = onClickCommunitySearch,
            onClickPostDetail = onClickPostDetail,
            onClickCommunityWrite = onClickCommunityWrite
        )
    }
}
