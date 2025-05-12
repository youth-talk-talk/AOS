package com.core.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.core.community.screen.write.CommunityWriteScreen
import com.core.navigation.navigator.CommunityWrite
import com.youthtalk.model.CommunityType

fun NavController.navigateCommunityWrite(communityType: CommunityType, navOptions: NavOptions? = null) {
    navigate(CommunityWrite(communityType), navOptions)
}

fun NavGraphBuilder.communityWriteNavigation() {
    composable<CommunityWrite> {
        val data = it.toRoute<CommunityWrite>()
        CommunityWriteScreen(
            communityType = data.communityType
        )
    }
}
