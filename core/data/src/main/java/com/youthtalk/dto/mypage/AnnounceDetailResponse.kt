package com.youthtalk.dto.mypage

import kotlinx.serialization.Serializable

@Serializable
data class AnnounceDetailResponse(
    val id: Long,
    val title: String,
    val content: String,
    val imageList: List<ImageInfoResponse>,
    val updateAt: String,
)

@Serializable
data class ImageInfoResponse(
    val id: Long,
    val imgUrl: String,
)
