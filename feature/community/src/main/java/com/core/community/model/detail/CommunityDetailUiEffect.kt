package com.core.community.model.detail

import com.core.base.model.UiEffect

sealed interface CommunityDetailUiEffect : UiEffect {
    data object ShowSnackBarDeleteComment : CommunityDetailUiEffect
}
