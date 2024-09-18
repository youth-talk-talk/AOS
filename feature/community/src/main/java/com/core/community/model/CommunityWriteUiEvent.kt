package com.core.community.model

import com.youthtalk.model.SearchPolicy

sealed interface CommunityWriteUiEvent {
    data class SearchPolicies(val search: String) : CommunityWriteUiEvent
    data class SelectPolicy(val policy: SearchPolicy?) : CommunityWriteUiEvent
    data class ChangeTitleText(val text: String) : CommunityWriteUiEvent
    data class AddImage(val uri: String) : CommunityWriteUiEvent
    data class ChangeContents(val contentInfo: ContentInfo, val text: String) : CommunityWriteUiEvent
    data class DeleteImage(val index: Int) : CommunityWriteUiEvent
    data class CreatePost(val type: String) : CommunityWriteUiEvent
    data object DeleteText : CommunityWriteUiEvent
}
