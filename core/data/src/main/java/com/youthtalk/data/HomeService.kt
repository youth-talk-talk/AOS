package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.HomePoliciesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeService {

    @GET("/policies")
    suspend fun getPolices(
        @Query("categories") categories: List<String>,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): CommonResponse<HomePoliciesResponse>
}
