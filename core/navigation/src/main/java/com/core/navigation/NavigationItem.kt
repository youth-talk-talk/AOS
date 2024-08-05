package com.core.navigation

import androidx.annotation.DrawableRes
import com.core.navigation.NavigationRouteName.MAIN_COMMUNITY
import com.core.navigation.NavigationRouteName.MAIN_HOME
import com.core.navigation.NavigationRouteName.MAIN_MY_PAGE
import com.core.navigation.NavigationRouteName.POLICY_DETAIL
import com.youth.app.core.navigation.R

sealed class MainNav(
    override val icon: Int,
    override val title: String,
    override val route: String,
) : IconDestination {
    data object Home : MainNav(
        route = MAIN_HOME,
        title = NavigationTitle.MAIN_HOME,
        icon = R.drawable.home,
    )

    data object Community : MainNav(
        route = MAIN_COMMUNITY,
        title = NavigationTitle.MAIN_COMMUNITY,
        icon = R.drawable.community,
    )

    data object MyPage : MainNav(
        route = MAIN_MY_PAGE,
        title = NavigationTitle.MAIN_MY_PAGE,
        icon = R.drawable.mypage,
    )

    companion object {
        fun isMainNavigation(route: String?): Boolean {
            return when (route) {
                MAIN_HOME, MAIN_COMMUNITY, MAIN_MY_PAGE -> true
                else -> false
            }
        }

        fun includeMainNav(route: String?): String {
            return when (route) {
                MAIN_HOME, POLICY_DETAIL -> MAIN_HOME
                MAIN_COMMUNITY -> MAIN_COMMUNITY
                MAIN_MY_PAGE -> MAIN_MY_PAGE
                else -> MAIN_HOME
            }
        }
    }
}

sealed class Nav(
    override val route: String,
) : Destination {

    data object PolicyDetail : Nav(
        route = POLICY_DETAIL,
    )
}

interface Destination {
    val route: String
}

interface IconDestination : Destination {
    override val route: String
    val title: String

    @get:DrawableRes
    val icon: Int
}

object NavigationRouteName {
    const val MAIN_HOME = "main_home"
    const val MAIN_COMMUNITY = "main_community"
    const val MAIN_MY_PAGE = "main_my_page"
    const val POLICY_DETAIL = "policy_detail"
}

object NavigationTitle {
    const val MAIN_HOME = "홈"
    const val MAIN_COMMUNITY = "커뮤니티"
    const val MAIN_MY_PAGE = "마이페이지"
}
