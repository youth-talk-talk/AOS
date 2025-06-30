package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportService {

    @POST("api/v1/report/post/{id}")
    suspend fun reportPosts(@Path("id") postId: Long): CommonResponse<Unit>

    @POST("api/v1/report/comments/{id}")
    suspend fun reportComments(@Path("id") commentId: Long): CommonResponse<Unit>
}
