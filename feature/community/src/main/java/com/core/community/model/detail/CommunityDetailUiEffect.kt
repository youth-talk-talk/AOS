package com.core.community.model.detail

import com.core.base.model.UiEffect

sealed interface CommunityDetailUiEffect : UiEffect {
    data object ShowSnackBarDeleteComment : CommunityDetailUiEffect
    data object ShowSnackBarModifyComment : CommunityDetailUiEffect
    data object ShowSnackBarDeletePost : CommunityDetailUiEffect
    data object ShowSnackBarReportPost : CommunityDetailUiEffect
    data class ShowSnackBarReportPostFail(val message: String?) : CommunityDetailUiEffect
}
