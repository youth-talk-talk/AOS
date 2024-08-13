package com.youthtalk.mapper

import com.youthtalk.dto.ReviewPostDataResponse
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
