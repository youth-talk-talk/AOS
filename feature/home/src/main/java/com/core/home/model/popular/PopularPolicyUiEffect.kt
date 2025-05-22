package com.core.home.model.popular

import com.core.base.model.UiEffect

sealed interface PopularPolicyUiEffect : UiEffect {
    data class ClickPolicy(val postId: Long) : PopularPolicyUiEffect
}
