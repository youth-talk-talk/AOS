package com.youthtalk.mapper

import com.youthtalk.dto.mypage.AnnounceDetailResponse
import com.youthtalk.dto.mypage.AnnounceResponse
import com.youthtalk.dto.mypage.ImageInfoResponse
import com.youthtalk.model.Announce
import com.youthtalk.model.AnnounceDetail
import com.youthtalk.model.ImageInfo

fun AnnounceResponse.toData() = Announce(
    id = id,
    title = title,
    updateAt = updateAt,
)

fun AnnounceDetailResponse.toData() = AnnounceDetail(
    id = id,
    title = title,
    content = content,
    imageList = imageList.map { it.toData() },
    updateAt = updateAt,
)

fun ImageInfoResponse.toData() = ImageInfo(
    id = id,
    imgUrl = imgUrl,
)
