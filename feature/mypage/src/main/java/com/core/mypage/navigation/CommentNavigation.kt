package com.core.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.core.mypage.screen.CommentScreenRoot
import com.core.navigation.model.CommentType
import com.core.navigation.navigator.Comment

fun NavController.navigateSettingComment(type: CommentType, navOptions: NavOptions? = null) {
    navigate(Comment(type), navOptions)
}

fun NavGraphBuilder.settingCommentNavigation(
    onBack: () -> Unit,
    showSnackBar: (String) -> Unit,
    onClickPostDetail: (Long) -> Unit,
    onClickPolicyDetail: (Long) -> Unit
) {
    composable<Comment> {
        CommentScreenRoot(
            onBack = onBack,
            showSnackBar = showSnackBar,
            onClickPostDetail = onClickPostDetail,
            onClickPolicyDetail = onClickPolicyDetail
        )
    }
}
