package com.core.mypage.model.account

sealed interface AccountUiEffect {
    data object Logout : AccountUiEffect
}
