package com.youthtalk.data

import com.youthtalk.model.CommonResponse
import com.youthtalk.model.TokenResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/login")
    suspend fun postLogin(@Body requestBody: RequestBody): CommonResponse<TokenResponse>
}
