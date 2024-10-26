package com.core.community.model

import com.youthtalk.model.PostType

sealed interface CommunityDetailUiEvent {
    data class GetPostDetail(val id: Long) : CommunityDetailUiEvent
    data class PostCommentLike(val id: Long, val isLike: Boolean) : CommunityDetailUiEvent
    data class PostAddComment(val id: Long, val text: String) : CommunityDetailUiEvent
    data class DeleteComment(val index: Int, val commentId: Long) : CommunityDetailUiEvent
    data class ModifyComment(val id: Long, val content: String) : CommunityDetailUiEvent
    data class PostScrap(val postId: Long, val isScrap: Boolean, val type: PostType) : CommunityDetailUiEvent
    data class DeletePost(val postId: Long) : CommunityDetailUiEvent
    data object ModifyPost : CommunityDetailUiEvent
}
