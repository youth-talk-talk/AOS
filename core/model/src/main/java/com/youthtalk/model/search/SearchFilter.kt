package com.youthtalk.model.search

import com.youthtalk.model.Category
import com.youthtalk.model.Region
import com.youthtalk.model.enum.EducationType
import com.youthtalk.model.enum.EmploymentType
import com.youthtalk.model.enum.MarriageType
import com.youthtalk.model.enum.SpecializedType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
    val region: List<Region>? = null,
    val minEarn: Int? = null,
    val maxEarn: Int? = null
) {
    fun toRequestBody() = Json.encodeToString(serializer(), this).toRequestBody()
}
