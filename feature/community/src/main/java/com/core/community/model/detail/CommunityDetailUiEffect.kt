package com.core.community.model.detail

import com.core.base.model.UiEffect

sealed interface CommunityDetailUiEffect : UiEffect {
    data object ShowSnackBarDeleteComment : CommunityDetailUiEffect
    data object ShowSnackBarModifyComment : CommunityDetailUiEffect
    data object ShowSnackBarDeletePost : CommunityDetailUiEffect
    data object ShowSnackBarReportPost : CommunityDetailUiEffect
    data object ShowSnackBarReportComment : CommunityDetailUiEffect
    data class ShowSnackBarBlockUser(val userName: String) : CommunityDetailUiEffect
    data class ShowSnackBarReportFail(val message: String?) : CommunityDetailUiEffect
    data class InitError(val message: String) : CommunityDetailUiEffect
}
