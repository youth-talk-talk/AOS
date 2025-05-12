package com.youth.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.core.navigation.navigator.CommunitySearch
import com.youth.search.screen.CommunitySearchScreen
import com.youthtalk.model.CommunityType

fun NavController.navigateCommunitySearch(communityType: CommunityType, navOptions: NavOptions? = null) {
    navigate(CommunitySearch(communityType), navOptions)
}

fun NavGraphBuilder.communitySearchNavigation(onBack: () -> Unit) {
    composable<CommunitySearch> {
        val communitySearch = it.toRoute<CommunitySearch>()
        CommunitySearchScreen(
            onBack = onBack,
            communityType = communitySearch.communityType
        )
    }
}
