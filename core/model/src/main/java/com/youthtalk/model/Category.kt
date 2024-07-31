package com.youthtalk.model

import kotlinx.serialization.Serializable

@Serializable
enum class Category(
    val categoryName: String
) {
    JOB("일자리"),
    EDUCATION("교육"),
    LIFE("생활지원"),
    PARTICIPATION("참여")
}
