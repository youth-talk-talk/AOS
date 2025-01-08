package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.MemberId
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/api/v1/login")
    suspend fun postLogin(@Body requestBody: RequestBody): CommonResponse<MemberId>

    @POST("/api/v1/signUp")
    suspend fun postSignUp(@Body requestBody: RequestBody): CommonResponse<Int>
}
