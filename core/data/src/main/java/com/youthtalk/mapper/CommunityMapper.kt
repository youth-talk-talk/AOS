package com.youthtalk.mapper

import com.youthtalk.dto.community.PostContentInfoResponse
import com.youthtalk.dto.community.PostContentRequest
import com.youthtalk.dto.community.PostCreatePostRequest
import com.youthtalk.dto.community.PostDetailResponse
import com.youthtalk.model.PostContentInfo
import com.youthtalk.model.PostDetail
import com.youthtalk.model.post.CreatePost
import com.youthtalk.model.post.PostContent
import java.time.LocalDateTime

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
    profileImage = profileImage,
    category = category,
    updatedAt = LocalDateTime.parse(updatedAt.replace(" ", "T")),
    scrap = scrap
)

fun PostContentInfoResponse.toData() = PostContentInfo(
    content = this.content,
    type = this.type
)

fun CreatePost.toData(): PostCreatePostRequest = PostCreatePostRequest(
    title = title,
    postType = postType,
    contentList = contentList.map { it.toData() },
    policyId = policyId
)

fun PostContent.toData(): PostContentRequest = PostContentRequest(
    content = content,
    type = type
)
