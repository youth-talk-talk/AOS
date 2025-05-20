package com.core.mypage.model.scrappost

import com.core.base.model.UiState

data class ScrapPostUiState(
    val isLoading: Boolean
) : UiState {
    companion object {
        val initState = ScrapPostUiState(
            isLoading = true
        )
    }
}
