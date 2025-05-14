package com.youthtalk.model.search

import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.EducationType
import com.youthtalk.model.typeenum.EmploymentType
import com.youthtalk.model.typeenum.MarriageType
import com.youthtalk.model.typeenum.SpecializedType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class SearchFilter(
    val keyword: String? = null,
    val category: List<Category>? = null,
    val marriage: MarriageType? = null,
    val age: String? = null,
    val education: List<EducationType>? = null,
    val employment: List<EmploymentType>? = null,
    val specialization: List<SpecializedType>? = null,
    val region: List<String>? = null,
    val minEarn: Int? = null,
    val maxEarn: Int? = null,
    val applyDue: String? = null
) {
    fun toRequestBody(): RequestBody {
        return Json.encodeToString(serializer(), this).toRequestBody()
    }

    fun isAllNull(): Boolean {
        return category == null && marriage == null && age == null &&
            education == null && employment == null && specialization == null &&
            region == null && minEarn == null && maxEarn == null
    }

    fun earnToString(): String? {
        if (maxEarn == null || minEarn == null) return null
        return if (minEarn == maxEarn) {
            return "${maxEarn}만" + if (maxEarn == 5000) " 이상" else ""
        } else {
            "${minEarn}만 ~ ${maxEarn}만 ${if (maxEarn == 5000) "이상" else ""}"
        }
    }
}
