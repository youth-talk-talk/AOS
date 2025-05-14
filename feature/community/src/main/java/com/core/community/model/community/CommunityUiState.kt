package com.core.community.model.community

import com.core.base.model.UiState

data class CommunityUiState(
    val count: Int
) : UiState {
    companion object {
        val initState = CommunityUiState(
            count = 0
        )
    }
}
