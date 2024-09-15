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
    @GET("/policies/{policyId}/comments")
    suspend fun getPolicyComment(@Path("policyId") policyId: String): CommonResponse<List<CommentResponse>>

    @DELETE("/comments/{commentId}")
    suspend fun postDeleteComment(@Path("commentId") commentId: Long): CommonResponse<PostAddCommentResponse>

    @GET("/members/me/comments")
    suspend fun getMyComments()

    @GET("/posts/{postId}/comments")
    suspend fun getPostDetailComments(@Path("postId") postId: Long): CommonResponse<List<CommentResponse>>

    @POST("/comments/likes")
    suspend fun postLikes(@Body requestBody: RequestBody): CommonResponse<Unit>

    @PATCH("/comments")
    suspend fun patchComment(@Body requestBody: RequestBody): CommonResponse<Unit>
}
