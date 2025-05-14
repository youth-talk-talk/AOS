package com.youthtalk.mapper

import com.youthtalk.dto.community.PostContentInfoResponse
import com.youthtalk.dto.community.PostDetailResponse
import com.youthtalk.model.PostContentInfo
import com.youthtalk.model.PostDetail

fun PostDetailResponse.toData() = PostDetail(
    postId = postId,
    postType = postType,
    title = title,
    contentList = contentList.map { it.toData() },
    policyId = policyId,
    policyTitle = policyTitle,
    writerId = writerId,
    nickname = nickname,
    view = view,
    images = images,
    category = category,
    scrap = scrap
)

fun PostContentInfoResponse.toData() = PostContentInfo(
    content = this.content,
    type = this.type
)
