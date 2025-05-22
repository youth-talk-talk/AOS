package com.feature.policydetail.model

import com.core.base.model.UiEvent
import com.youthtalk.model.comment.Comment

sealed interface PolicyDetailUiEvent : UiEvent {
    data class InitData(val policyId: Long) : PolicyDetailUiEvent
    data class LinkUrl(val url: String) : PolicyDetailUiEvent
    data class Shared(val url: String) : PolicyDetailUiEvent
    data class ChangeDetailType(val type: PolicyDetailType, val commentId: Long = 0L, val message: String = "") : PolicyDetailUiEvent
    data class PatchModifyComment(val commentId: Long, val message: String) : PolicyDetailUiEvent
    data class PostAddPolicyComment(val policyId: Long, val message: String) : PolicyDetailUiEvent
    data class PostDeleteComment(val comment: Comment) : PolicyDetailUiEvent
    data class PolicyScrap(val policyId: Long, val scrap: Boolean) : PolicyDetailUiEvent
    data class PostCommentLike(val commentId: Long, val isLike: Boolean) : PolicyDetailUiEvent
}
