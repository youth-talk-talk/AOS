package com.youthtalk.data

import com.youthtalk.dto.CommentResponse
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PostAddCommentResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentService {
    @GET("/policies/{policyId}/comments")
    suspend fun getPolicyComment(@Path("policyId") policyId: String): CommonResponse<List<CommentResponse>>

    @DELETE("/comments/{commentId}")
    suspend fun postDeleteComment(@Path("commentId") commentId: Long): CommonResponse<PostAddCommentResponse>
}
