package com.feature.policydetail.model

import com.core.base.model.UiEffect

sealed interface PolicyDetailUiEffect : UiEffect {
    data class LinkBrowser(val url: String) : PolicyDetailUiEffect
    data class Share(val url: String) : PolicyDetailUiEffect
    data class ChangeComment(val commentId: Long, val message: String) : PolicyDetailUiEffect
    data object ShowSnackBarDeleteComment : PolicyDetailUiEffect
    data object ShowSnackBarModifyComment : PolicyDetailUiEffect
}
