package com.youthtalk.data

import com.youthtalk.dto.CommentResponse
import com.youthtalk.dto.CommonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentService {
    @GET("/policies/{policyId}/comments")
    suspend fun getPolicyComment(@Path("policyId") policyId: String): CommonResponse<List<CommentResponse>>
}
