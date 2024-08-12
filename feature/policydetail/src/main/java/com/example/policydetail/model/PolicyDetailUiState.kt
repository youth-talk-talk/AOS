package com.example.policydetail.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.youthtalk.model.Comment
import com.youthtalk.model.PolicyDetail
import com.youthtalk.model.Region
import com.youthtalk.model.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
interface PolicyDetailUiState {

    @Immutable
    object Loading : PolicyDetailUiState

    @Immutable
    data class Success(
        val policyDetail: PolicyDetail,
        val myInfo: User,
        val comments: ImmutableList<Comment> = persistentListOf(),
    ) : PolicyDetailUiState {
        companion object {
            val defaultDetail = PolicyDetail(
                title = "",
                introduction = "",
                supportDetail = "",
                applyTerm = "",
                operationTerm = "",
                age = "",
                addrIncome = "",
                education = "",
                major = "",
                employment = "",
                specialization = "",
                applLimit = "",
                addition = "",
                applStep = "",
                evaluation = "",
                applUrl = "",
                submitDoc = "",
                etc = "",
                hostDep = "",
                operatingOrg = "",
                refUrl1 = "",
                refUrl2 = "",
                formattedApplUrl = "",
                isScrap = false,
            )

            val user = User(
                memberId = 0,
                nickname = "",
                region = Region.ALL,
            )
        }
    }
}
