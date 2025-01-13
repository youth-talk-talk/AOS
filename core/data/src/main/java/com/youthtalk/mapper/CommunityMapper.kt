package com.youthtalk.mapper

import com.youthtalk.dto.PostDataResponse
import com.youthtalk.dto.ScrapPostDataResponse
import com.youthtalk.dto.community.PostContentInfoResponse
import com.youthtalk.dto.community.PostDetailResponse
import com.youthtalk.model.Post
import com.youthtalk.model.PostContentInfo
import com.youthtalk.model.PostDetail
import com.youthtalk.model.ReviewPost
import com.youthtalk.model.ScrapPost

fun PostDataResponse.toData() = Post(
    postId = postId,
    title = title,
    writerId = writerId,
    scraps = scraps,
    scrap = scrap,
    comments = comments,
    policyId = policyId,
    policyTitle = policyTitle,
)

fun PostDataResponse.toReviewData() = ReviewPost(
    postId = postId,
    title = title,
    writerId = writerId,
    scraps = scraps,
    scrap = scrap,
    comments = comments,
    policyId = policyId,
    policyTitle = policyTitle,
)

fun ScrapPostDataResponse.toData() = ScrapPost(
    scrapId = scrapId,
    postId = postId,
    title = title,
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
