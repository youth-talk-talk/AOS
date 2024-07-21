package com.youthtalk.data

import com.youthtalk.model.CommonResponse
import com.youthtalk.model.UserResponse
import retrofit2.http.GET

interface UserService {

    @GET("/members/me")
    suspend fun getUser(): CommonResponse<UserResponse>
}
