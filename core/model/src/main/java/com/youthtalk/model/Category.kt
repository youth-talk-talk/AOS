package com.youthtalk.model

import androidx.annotation.DrawableRes

data class CategoryInfo(
    @DrawableRes val icon: Int,
    val description: Category,
)

enum class Category(val categoryName: String) {
    JOB("일자리"),
    EDUCATION("교육"),
    LIFE("생활지원"),
    PARTICIPATION("참여")
}
