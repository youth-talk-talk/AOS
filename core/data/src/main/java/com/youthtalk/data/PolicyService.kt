package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.dto.PostAddCommentResponse
import com.youthtalk.dto.home.HomeDataResponse
import com.youthtalk.dto.home.NewPoliciesResponse
import com.youthtalk.dto.policy.PolicyResponse
import com.youthtalk.dto.specpolicy.SpecPoliciesResponse
import com.youthtalk.model.typeenum.SortType
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PolicyService {

    @GET("/api/v1/home")
    suspend fun getHome(@Query("sort") sort: SortType = SortType.RECENT): CommonResponse<HomeDataResponse>

    @GET("/api/v1/home/new-policies")
    suspend fun getNewPolicies(@Query("sort") sort: SortType = SortType.RECENT): CommonResponse<NewPoliciesResponse>

    @GET("/api/v1/policies/{policyId}")
    suspend fun getPolicyDetail(@Path("policyId") policyId: Long): CommonResponse<PolicyDetailResponse>

    @POST("/api/v1/policies/search")
    suspend fun postSpecPolicies(
        @Body requestBody: RequestBody,
        @Query("sort") sort: SortType = SortType.RECENT,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): CommonResponse<SpecPoliciesResponse>

    @POST("/api/v1/policies/{id}/scrap")
    suspend fun postPolicyScrap(@Path("id") id: Long): CommonResponse<Unit>

    @POST("/api/v1/policies/comments")
    suspend fun postAddComment(@Body requestBody: RequestBody): CommonResponse<PostAddCommentResponse>

    @GET("/api/v1/policies/recent-view")
    suspend fun getRecentlyViewPolicies(): CommonResponse<List<com.youthtalk.dto.policy.PolicyResponse>>

    @GET("/api/v1/policies/scrap")
    suspend fun getScrapPolicies(@Query("page") page: Int, @Query("size") size: Int): CommonResponse<List<PolicyResponse>>
}
