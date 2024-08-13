package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.ReviewPostResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CommunityService {

    @POST("/posts/review")
    suspend fun postReviewPosts(
        @Body requestBody: RequestBody,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): CommonResponse<ReviewPostResponse>
}
