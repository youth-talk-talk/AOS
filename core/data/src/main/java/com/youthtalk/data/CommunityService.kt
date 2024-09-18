package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PostAddCommentResponse
import com.youthtalk.dto.PostDataResponse
import com.youthtalk.dto.PostResponse
import com.youthtalk.dto.PostSearchResponse
import com.youthtalk.dto.community.PostDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("/posts/keyword")
    suspend fun getSearchPosts(
        @Query("keyword") keyword: String,
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): CommonResponse<PostSearchResponse>

    @POST("/posts/{id}/scrap")
    suspend fun postPostScrap(@Path("id") id: Long): CommonResponse<Unit>

    @GET("/posts/{id}")
    suspend fun getPostDetail(@Path("id") id: Long): CommonResponse<PostDetailResponse>

    @POST("/posts/comments")
    suspend fun postPostAddComment(@Body requestBody: RequestBody): CommonResponse<PostAddCommentResponse>

    @Multipart
    @POST("/posts/image")
    suspend fun postUploadImage(@Part image: MultipartBody.Part): CommonResponse<String>

    @POST("/posts/create")
    suspend fun postCreate(@Body requestBody: RequestBody): CommonResponse<PostDetailResponse>
}
