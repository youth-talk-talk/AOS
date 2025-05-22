package com.core.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.community.screen.write.CommunityWriteScreen
import com.core.navigation.navigator.CommunityWrite
import com.youthtalk.model.post.PostSubject

fun NavController.navigateCommunityWrite(communityType: PostSubject, postId: Long? = null, navOptions: NavOptions? = null) {
    navigate(CommunityWrite(postId, communityType), navOptions)
}

fun NavGraphBuilder.communityWriteNavigation(
    checkPermission: (String) -> Boolean,
    onBack: () -> Unit,
    onCreate: (PostSubject) -> Unit,
    onModify: (Long) -> Unit
) {
    composable<CommunityWrite> {
        CommunityWriteScreen(
            checkPermission = checkPermission,
            onBack = onBack,
            onCreate = onCreate,
            onModify = onModify
        )
    }
}
