package com.youth.search.model.communitysearch

import com.core.base.model.UiEvent
import com.youth.search.model.SearchState
import com.youthtalk.model.post.PostSubject

sealed interface CommunityUiEvent : UiEvent {
    data class InitData(val communityType: PostSubject) : CommunityUiEvent
    data class SetState(val state: SearchState) : CommunityUiEvent
    data class Search(val keyword: String, val communityType: PostSubject) : CommunityUiEvent
    data class SetRecently(val list: List<String>) : CommunityUiEvent
}
