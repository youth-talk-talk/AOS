package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PostDataResponse
import com.youthtalk.dto.PostResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityService {

    @GET("/posts/review")
    suspend fun postReviewPosts(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("categories") categories: List<String>,
    ): CommonResponse<PostResponse>

    @GET("/posts/post")
    suspend fun getPosts(@Query("page") page: Int, @Query("size") size: Int): CommonResponse<PostResponse>

    @GET("/posts/{type}")
    suspend fun getMyPagePosts(
        @Path("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): CommonResponse<List<PostDataResponse>>
}
