package com.youthtalk.dto

import com.youthtalk.model.Category
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class ReviewPostRequest(
    val categories: List<Category>,
) {
    fun toRequestBody() = Json.encodeToString(this).toRequestBody()
}
