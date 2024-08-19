package com.youthtalk.mapper

import com.youthtalk.dto.PostDataResponse
import com.youthtalk.dto.ReviewPostDataResponse
import com.youthtalk.model.Post
import com.youthtalk.model.ReviewPost

fun ReviewPostDataResponse.toData() = ReviewPost(
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
