package com.core.mypage.model.scrappolicy

interface ScrapPolicyUiEvent {
    data class DeleteScrap(
        val policyId: String,
        val scrap: Boolean,
    ) : ScrapPolicyUiEvent
}
