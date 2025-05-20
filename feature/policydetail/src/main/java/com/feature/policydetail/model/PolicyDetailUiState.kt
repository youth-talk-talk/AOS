package com.feature.policydetail.model

import com.core.base.model.UiState
import com.youthtalk.model.CommentInfo
import com.youthtalk.model.PolicyDetail
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region

data class PolicyDetailUiState(
    val isLoading: Boolean,
    val policyId: Long,
    val policyDetail: PolicyDetail,
    val commentInfo: CommentInfo,
    val user: User,
    val detailType: PolicyDetailType = PolicyDetailType.MAIN
) : UiState {
    companion object {
        val initState = PolicyDetailUiState(
            isLoading = true,
            policyDetail = PolicyDetail(
                departmentImgUrl = null,
                recruitmentType = "",
                region = Region.ALL,
                subRegion = null,
                category = Category.ALL,
                title = "",
                introduction = "",
                supportDetail = "",
                applyTerm = "",
                age = "",
                education = null,
                major = null,
                employment = null,
                specialization = null,
                applLimit = "",
                addition = "",
                applStep = "",
                evaluation = "",
                applUrl = "",
                submitDoc = "",
                etc = null,
                hostDep = "",
                refUrl1 = null,
                refUrl2 = null,
                isScrap = false,
                earnEtc = null,
                marriage = null
            ),
            commentInfo = CommentInfo(
                commentCount = 0,
                comments = listOf()
            ),
            user = User(
                memberId = 0,
                nickname = "",
                profileImgUrl = null,
                region = Region.ALL
            ),
            detailType = PolicyDetailType.MAIN,
            policyId = 0L
        )
    }
}
