package com.youthtalk.mapper

import com.youthtalk.dto.PostDataResponse
import com.youthtalk.model.Post

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
