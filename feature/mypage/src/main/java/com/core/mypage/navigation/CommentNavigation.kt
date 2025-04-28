package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.core.mypage.screen.CommentScreen
import com.core.navigation.model.CommentType
import com.core.navigation.navigator.Comment

fun NavController.navigateSettingComment(type: CommentType, navOptions: NavOptions? = null) {
    navigate(Comment(type), navOptions)
}

fun NavGraphBuilder.settingCommentNavigation() {
    composable<Comment> {
        val route = it.toRoute<Comment>()
        CommentScreen(
            type = route.type,
        )
    }
}
