package com.youthtalk.data

import com.youthtalk.dto.CommentResponse
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PostAddCommentResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {
    @GET("/api/v1/policies/{policyId}/comments")
    suspend fun getPolicyComment(@Path("policyId") policyId: String): CommonResponse<List<CommentResponse>>

    @DELETE("/api/v1/comments/{commentId}")
    suspend fun postDeleteComment(@Path("commentId") commentId: Long): CommonResponse<PostAddCommentResponse>

    @GET("/api/v1/members/me/comments")
    suspend fun getMyComments()

    @GET("/api/v1/posts/{postId}/comments")
    suspend fun getPostDetailComments(@Path("postId") postId: Long): CommonResponse<List<CommentResponse>>

    @POST("/api/v1/comments/likes")
    suspend fun postLikes(@Body requestBody: RequestBody): CommonResponse<Unit>

    @PATCH("/api/v1/comments")
    suspend fun patchComment(@Body requestBody: RequestBody): CommonResponse<Unit>
}
