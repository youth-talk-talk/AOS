package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PostAddCommentResponse
import com.youthtalk.dto.comment.CommentInfoResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {
    @GET("/api/v1/policies/{policyId}/comments")
    suspend fun getPolicyComment(@Path("policyId") policyId: Long): CommonResponse<CommentInfoResponse>

    @DELETE("/api/v1/comments/{commentId}")
    suspend fun postDeleteComment(@Path("commentId") commentId: Long): CommonResponse<PostAddCommentResponse>

    @GET("/api/v1/members/me/comments")
    suspend fun getMyComments()

    @POST("/api/v1/posts/comments")
    suspend fun postPostAddComment(@Body requestBody: RequestBody): CommonResponse<PostAddCommentResponse>

    @GET("/api/v1/posts/{postId}/comments")
    suspend fun getPostDetailComments(@Path("postId") postId: Long): CommonResponse<CommentInfoResponse>

    @POST("/api/v1/comments/likes")
    suspend fun postLikes(@Body requestBody: RequestBody): CommonResponse<Unit>

    @PATCH("/api/v1/comments")
    suspend fun patchComment(@Body requestBody: RequestBody): CommonResponse<Unit>
}
