package com.youthtalk.mapper

import com.youthtalk.dto.community.PostContentInfoResponse
import com.youthtalk.dto.community.PostContentRequest
import com.youthtalk.dto.community.PostCreatePostRequest
import com.youthtalk.dto.community.PostDetailResponse
import com.youthtalk.dto.community.PostModifyPostRequest
import com.youthtalk.model.post.CreatePost
import com.youthtalk.model.post.ModifyPost
import com.youthtalk.model.post.PostContent
import com.youthtalk.model.post.PostContentInfo
import com.youthtalk.model.post.PostDetail
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

fun ModifyPost.toData(): PostModifyPostRequest = PostModifyPostRequest(
    title = title,
    postType = postType,
    policyId = policyId,
    contentList = contentList.map { it.toData() },
    addImgUrlList = addImgUrlList,
    deletedImgUrlList = deletedImgUrlList
)

fun PostContent.toData(): PostContentRequest = PostContentRequest(
    content = content,
    type = type
)
