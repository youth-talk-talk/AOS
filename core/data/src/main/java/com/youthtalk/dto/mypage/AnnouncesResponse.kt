package com.youthtalk.dto.mypage

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncesResponse(
    val pageNum: Int,
    val pageSize: Int,
    val totalPage: Int,
    val announcementList: List<AnnounceResponse>,
)

@Serializable
data class AnnounceResponse(
    val id: Long,
    val title: String,
    val updateAt: String,
)
