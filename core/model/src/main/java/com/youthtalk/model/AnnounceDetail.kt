package com.youthtalk.model

data class AnnounceDetail(
    val id: Long,
    val title: String,
    val content: String,
    val imageList: List<ImageInfo>,
    val updateAt: String,
)

data class ImageInfo(
    val id: Long,
    val imgUrl: String,
)
