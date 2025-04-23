package com.core.model.login

sealed interface LoginUiEffect {
    data object GoMainActivity : LoginUiEffect
    data object GoLoginActivity : LoginUiEffect
}
