package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PostAddCommentResponse
import com.youthtalk.dto.PostResponse
import com.youthtalk.dto.PostSearchResponse
import com.youthtalk.dto.community.PostDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityService {

    @GET("/api/v1/posts/review")
    suspend fun postReviewPosts(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("categories") categories: List<String>
    ): CommonResponse<PostResponse>

    @GET("/api/v1/posts/post")
    suspend fun getPosts(@Query("page") page: Int, @Query("size") size: Int): CommonResponse<PostResponse>

    @GET("/api/v1/posts/keyword")
    suspend fun getSearchPosts(
        @Query("keyword") keyword: String,
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): CommonResponse<PostSearchResponse>

    @POST("/api/v1/posts/{id}/scrap")
    suspend fun postPostScrap(@Path("id") id: Long): CommonResponse<Unit>

    @GET("/api/v1/posts/{id}")
    suspend fun getPostDetail(@Path("id") id: Long): CommonResponse<PostDetailResponse>

    @POST("/api/v1/posts/comments")
    suspend fun postPostAddComment(@Body requestBody: RequestBody): CommonResponse<PostAddCommentResponse>

    @Multipart
    @POST("/api/v1/posts/image")
    suspend fun postUploadImage(@Part image: MultipartBody.Part): CommonResponse<String>

    @POST("/api/v1/posts")
    suspend fun postCreate(@Body requestBody: RequestBody): CommonResponse<PostDetailResponse>

    @PATCH("/api/v1/posts/{id}")
    suspend fun postModifyPost(@Path("id") id: Long, @Body requestBody: RequestBody): CommonResponse<PostDetailResponse>

    @DELETE("/api/v1/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long): CommonResponse<Unit>
}
