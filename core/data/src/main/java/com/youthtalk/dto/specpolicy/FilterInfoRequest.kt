package com.youthtalk.dto.specpolicy

import com.youthtalk.model.Category
import com.youthtalk.model.Region
import com.youthtalk.model.typeenum.EducationType
import com.youthtalk.model.typeenum.EmploymentType
import com.youthtalk.model.typeenum.MarriageType
import com.youthtalk.model.typeenum.SpecializedType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class FilterInfoRequest(
    val category: List<Category>? = null,
    val marriage: MarriageType? = null,
    val age: String? = null,
    val education: List<EducationType>? = null,
    val employment: List<EmploymentType>? = null,
    val specialization: List<SpecializedType>? = null,
    val region: List<Region>? = null,
    val minEarn: Int? = null,
    val maxEarn: Int? = null,
    val isFinished: Boolean? = null,
    val keyword: String? = null
) {
    fun toRequestBody() = Json.encodeToString(serializer(), this).toRequestBody()
}
