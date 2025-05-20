package com.core.community.model.detail

import com.core.base.model.UiState
import com.youthtalk.model.CommentInfo
import com.youthtalk.model.PostDetail
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import java.time.LocalDateTime

data class CommunityDetailUiState(
    val initLoading: Boolean,
    val user: User,
    val postDetail: PostDetail,
    val comments: CommentInfo,
    val detailType: CommunityDetailType
) : UiState {
    companion object {
        val initState = CommunityDetailUiState(
            user = User(
                memberId = 0L,
                region = Region.ALL,
                profileImgUrl = null,
                nickname = ""
            ),
            postDetail = PostDetail(
                postId = 0,
                postType = "",
                title = "",
                contentList = listOf(),
                policyId = null,
                policyTitle = null,
                writerId = 0,
                nickname = null,
                view = 0,
                profileImage = null,
                category = Category.JOB,
                updatedAt = LocalDateTime.now(),
                scrap = false

            ),
            comments = CommentInfo(
                commentCount = 0,
                comments = listOf()
            ),
            detailType = CommunityDetailType.MAIN,
            initLoading = true
        )
    }
}
