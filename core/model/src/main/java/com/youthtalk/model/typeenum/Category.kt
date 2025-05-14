package com.youthtalk.model.typeenum

import kotlinx.serialization.Serializable

@Serializable
enum class Category(
    val categoryName: String
) {
    ALL("전체"),
    DWELLING("주거"),
    EDUCATION("교육"),
    JOB("일자리"),
    LIFE("복지"),
    PARTICIPATION("참여 권리")
}
