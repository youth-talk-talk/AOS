package com.core.mypage.model.home

sealed interface MyPageHomeUiEffect {
    data object GoBack : MyPageHomeUiEffect
    data object CloseRegionBottomSheet : MyPageHomeUiEffect
    data object GoLogin : MyPageHomeUiEffect
}
