package com.youthtalk.data

import com.youthtalk.dto.CommentResponse
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.UserResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {

    @GET("/members/me")
    suspend fun getUser(): CommonResponse<UserResponse>

    @GET("/members/me/comments/likes")
    suspend fun getLikeComments(): CommonResponse<List<CommentResponse>>

    @GET("/members/me/comments")
    suspend fun getMyComments(): CommonResponse<List<CommentResponse>>

    @PATCH("/members/me")
    suspend fun postUser(@Body requestBody: RequestBody): CommonResponse<UserResponse>

    @POST("/members/me")
    suspend fun postDeleteUser(): CommonResponse<Int>
}
