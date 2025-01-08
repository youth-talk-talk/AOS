package com.youthtalk.data

import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.mypage.AnnounceDetailResponse
import com.youthtalk.dto.mypage.AnnouncesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnnounceService {

    /**
     * 회원 > 회원 탈퇴
     * @see <a href="https://web.postman.co/workspace/My-Workspace~3c068cce-5f49-44cd-8661-9bdd02a8cf87/request/25820857-12e73469-506e-4539-9a3a-5b199dd1a548?action=share&source=copy-link&creator=25820857">PostMan 링크</a>
     * @return [Unit]
     * */
    @GET("/api/v1/announcements")
    suspend fun getAllAnnounce(@Query("size") size: Int, @Query("page") page: Int): CommonResponse<AnnouncesResponse>

    @GET("/api/v1/announcements/{id}")
    suspend fun getAnnounceDetail(@Path("id") id: Long): CommonResponse<AnnounceDetailResponse>
}
