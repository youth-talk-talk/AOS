package com.example.navigation

import androidx.annotation.DrawableRes
import com.youth.app.core.navigation.R

sealed class MainNav(
    override val icon: Int,
    override val title: String,
    override val route: String,
) : Destination {
    data object Home : MainNav(
        route = NavigationRouteName.MAIN_HOME,
        title = NavigationTitle.MAIN_HOME,
        icon = R.drawable.home,
    )

    data object Community : MainNav(
        route = NavigationRouteName.MAIN_COMMUNITY,
        title = NavigationTitle.MAIN_COMMUNITY,
        icon = R.drawable.community,
    )

    data object MyPage : MainNav(
        route = NavigationRouteName.MAIN_MY_PAGE,
        title = NavigationTitle.MAIN_MY_PAGE,
        icon = R.drawable.mypage,
    )
}

interface Destination {
    val route: String
    val title: String

    @get:DrawableRes
    val icon: Int
}

object NavigationRouteName {
    const val MAIN_HOME = "main_home"
    const val MAIN_COMMUNITY = "main_community"
    const val MAIN_MY_PAGE = "main_my_page"
}

object NavigationTitle {
    const val MAIN_HOME = "홈"
    const val MAIN_COMMUNITY = "커뮤니티"
    const val MAIN_MY_PAGE = "마이페이지"
}
