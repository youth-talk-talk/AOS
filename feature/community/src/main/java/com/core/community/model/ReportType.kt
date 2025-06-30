package com.core.community.model

sealed interface ReportType {
    data object Post : ReportType
    data object Comment : ReportType
}
