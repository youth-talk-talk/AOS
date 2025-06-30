package com.core.community.model

sealed interface ReportType {
    data object Post : ReportType
    data class Comment(val commentId: Long) : ReportType
}
