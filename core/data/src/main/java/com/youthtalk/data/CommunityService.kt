package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PostResponse
import com.youthtalk.dto.ReviewPostResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CommunityService {

    @GET("/posts/review")
    suspend fun postReviewPosts(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("categories") categories: List<String>,
    ): CommonResponse<ReviewPostResponse>

    @GET("/posts/post")
    suspend fun getPosts(@Query("page") page: Int, @Query("size") size: Int): CommonResponse<PostResponse>
}
