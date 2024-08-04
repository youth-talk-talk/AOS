package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.UserResponse
import retrofit2.http.GET

interface UserService {

    @GET("/members/me")
    suspend fun getUser(): CommonResponse<UserResponse>
}
