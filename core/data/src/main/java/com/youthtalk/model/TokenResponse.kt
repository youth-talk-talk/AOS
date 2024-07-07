package com.youthtalk.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody.Companion.toResponseBody

data class TokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
) {
    fun toResponseBody() = Gson().toJson(this).toResponseBody()
}
