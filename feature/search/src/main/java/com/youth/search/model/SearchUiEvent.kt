package com.youth.search.model

sealed interface SearchUiEvent {
    data class ClickRecently(val search: String) : SearchUiEvent
    data class DeleteRecently(val search: String) : SearchUiEvent
}
