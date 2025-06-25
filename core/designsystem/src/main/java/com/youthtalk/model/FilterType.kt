package com.youthtalk.model

import com.youthtalk.model.search.SearchFilter

enum class FilterType(val title: String) {
    POLICY_TYPE("정책분야"),
    REGION("지역"),
    RECRUIT("취업상태"),
    EDUCATION("학력"),
    SPECIALIZED("특화 분야"),
    AGE_EARN("연령 및 소득")
}

fun FilterType.getCountFrom(filter: SearchFilter): Int = when (this) {
    FilterType.POLICY_TYPE -> filter.category?.size ?: 0
    FilterType.REGION -> filter.region?.size ?: 0
    FilterType.RECRUIT -> filter.employment?.size ?: 0
    FilterType.EDUCATION -> filter.education?.size ?: 0
    FilterType.SPECIALIZED -> {
        if (filter.specialization.isNullOrEmpty()) {
            0
        } else {
            val specialCount = filter.specialization?.size ?: 0
            val marriedCount = if (filter.marriage != null) 1 else 0
            specialCount + marriedCount
        }
    }

    FilterType.AGE_EARN -> {
        val earnCount = if (filter.minEarn != null || filter.maxEarn != null) 1 else 0
        val ageCount = if (filter.age != null) 1 else 0
        earnCount + ageCount
    }
}
