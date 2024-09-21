package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.mypage.AnnounceDetailResponse
import com.youthtalk.dto.mypage.AnnouncesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnnounceService {

    @GET("/announcements")
    suspend fun getAllAnnounce(@Query("size") size: Int, @Query("page") page: Int): CommonResponse<AnnouncesResponse>

    @GET("/announcements/{id}")
    suspend fun getAnnounceDetail(@Path("id") id: Long): CommonResponse<AnnounceDetailResponse>
}
