package com.core.navigation

sealed class CommunityNavigation(
    val route: String,
) {
    data object Community : CommunityNavigation(
        route = CommunityRoute.COMMUNITY,
    )

    data object CommunityDetail : CommunityNavigation(
        route = CommunityRoute.COMMUNITY_DETAIL,
    )

    data object CommunityWrite : CommunityNavigation(
        route = CommunityRoute.COMMUNITY_WRITE,
    )
}

object CommunityRoute {
    const val COMMUNITY = "community"
    const val COMMUNITY_DETAIL = "community_detail"
    const val COMMUNITY_WRITE = "community_write"
}
