package com.youthtalk.data

import com.youthtalk.dto.CommentResponse
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.UserResponse
import retrofit2.http.GET

interface UserService {

    @GET("/members/me")
    suspend fun getUser(): CommonResponse<UserResponse>

    @GET("/members/me/comments/likes")
    suspend fun getLikeComments(): CommonResponse<List<CommentResponse>>

    @GET("/members/me/comments")
    suspend fun getMyComments(): CommonResponse<List<CommentResponse>>
}
