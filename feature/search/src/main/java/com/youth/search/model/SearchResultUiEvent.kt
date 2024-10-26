package com.youth.search.model

import com.youthtalk.model.FilterInfo
import com.youthtalk.model.PostType

sealed interface SearchResultUiEvent {
    data class GetPolicies(val keyword: String) : SearchResultUiEvent
    data class GetPost(val type: String, val keyword: String) : SearchResultUiEvent
    data class PostPolicyScrap(val postId: String, val scrap: Boolean) : SearchResultUiEvent
    data class PostFilterInfo(val filterInfo: FilterInfo) : SearchResultUiEvent
    data object GetFilterInfo : SearchResultUiEvent
    data class FilterApply(val search: String) : SearchResultUiEvent
    data class PostPostScrap(val postId: Long, val scrap: Boolean, val type: PostType) : SearchResultUiEvent
}
