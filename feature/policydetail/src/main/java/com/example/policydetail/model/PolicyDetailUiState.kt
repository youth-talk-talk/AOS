package com.example.policydetail.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.youthtalk.model.PolicyDetail

@Stable
interface PolicyDetailUiState {

    @Immutable
    object Loading : PolicyDetailUiState

    @Immutable
    data class Success(
        val policyDetail: PolicyDetail,
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
        }
    }
}
