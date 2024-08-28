package com.youthtalk.dto.specpolicy

import com.youthtalk.model.Category
import com.youthtalk.model.EmploymentCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class FilterInfoRequest(
    val categories: List<Category>?,
    val age: Int?,
    val employmentCodeList: List<EmploymentCode>?,
    val isFinished: Boolean?,
    val keyword: String?,
) {
    fun toRequestBody() = Json.encodeToString(serializer(), this).toRequestBody()
}
