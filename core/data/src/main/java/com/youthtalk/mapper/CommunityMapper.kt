package com.youthtalk.mapper

import com.youthtalk.dto.PostDataResponse
import com.youthtalk.dto.community.PostContentInfoResponse
import com.youthtalk.dto.community.PostDetailResponse
import com.youthtalk.model.Post
import com.youthtalk.model.PostContentInfo
import com.youthtalk.model.PostDetail

fun PostDataResponse.toData() = Post(
    postId = postId,
    title = title,
    content = content,
    writerId = writerId,
    scraps = scraps,
    scrap = scrap,
    comments = comments,
    policyId = policyId,
    policyTitle = policyTitle,
)

fun PostDetailResponse.toData() = PostDetail(
    postId = postId,
    postType = postType,
    title = title,
    content = content,
    contentList = contentList.map { it.toData() },
    policyId = policyId,
    policyTitle = policyTitle,
    writerId = writerId,
    nickname = nickname,
    view = view,
    images = images,
    category = category,
    scrap = scrap,
)

fun PostContentInfoResponse.toData() = PostContentInfo(
    content = this.content,
    type = this.type,
)
