package com.youthtalk.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

data class LoginRequest(
    @SerializedName("username")
    val username: String,
) {
    fun toRequestBody(): RequestBody {
        return Gson().toJson(this).toRequestBody()
    }
}
