package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.UserResponse
import com.youthtalk.dto.comment.SettingCommentInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {
    @GET("/api/v1/members/me")
    suspend fun getUser(): CommonResponse<UserResponse>

    @GET("/api/v1/members/me/comments/likes")
    suspend fun getLikeComments(): CommonResponse<SettingCommentInfoResponse>

    @GET("/api/v1/members/me/comments")
    suspend fun getMyComments(): CommonResponse<SettingCommentInfoResponse>

    @PATCH("/api/v1/members/me")
    suspend fun postUser(@Body requestBody: RequestBody): CommonResponse<UserResponse>

    @POST("/api/v1/members/me")
    suspend fun postDeleteUser(): CommonResponse<Unit>

    @Multipart
    @POST("/api/v1/members/profile")
    suspend fun postUserImage(@Part image: MultipartBody.Part): CommonResponse<String>

    @DELETE
    suspend fun deleteUserImage(): CommonResponse<Unit>
}
