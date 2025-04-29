package com.core.mypage.model.etc

sealed interface EtcUiEffect {
    data object Logout : EtcUiEffect
}
