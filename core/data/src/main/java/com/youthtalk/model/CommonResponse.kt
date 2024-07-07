package com.youthtalk.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody.Companion.toResponseBody

data class CommonResponse<T>(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val data: T,
) {
    fun toResponseBody() = Gson().toJson(this).toResponseBody()
}
