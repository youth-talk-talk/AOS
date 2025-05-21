package com.core.mypage.model.comment

import com.core.base.model.UiState
import com.core.navigation.model.CommentType
import com.youthtalk.model.User
import com.youthtalk.model.comment.SettingCommentInfo
import com.youthtalk.model.typeenum.Region

data class CommentUiState(
    val isLoading: Boolean,
    val user: User,
    val commentInfo: SettingCommentInfo,
    val commentScreenType: SettingCommentScreenType,
    val commentType: CommentType
) : UiState {
    companion object {
        val initState = CommentUiState(
            isLoading = true,
            commentInfo = SettingCommentInfo(
                commentCount = 0,
                comments = listOf()
            ),
            commentScreenType = SettingCommentScreenType.MAIN,
            commentType = CommentType.MY,
            user = User(
                memberId = 0,
                nickname = "",
                profileImgUrl = null,
                region = Region.ALL
            )
        )
    }
}
