package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.HomePoliciesResponse
import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.dto.specpolicy.SpecPoliciesResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PolicyService {

    @GET("/policies")
    suspend fun getPolices(
        @Query("categories") categories: List<String>,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): CommonResponse<HomePoliciesResponse>

    @GET("/policies/{policyid}")
    suspend fun getPolicyDetail(@Path("policyid") policyId: String): CommonResponse<PolicyDetailResponse>

    @POST("/policies/search")
    suspend fun postSpecPolicies(
        @Body requestBody: RequestBody,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): CommonResponse<SpecPoliciesResponse>

    @POST("policies/{id}/scrap")
    suspend fun postPolicyScrap(@Path("id") id: String): CommonResponse<Unit>
}
