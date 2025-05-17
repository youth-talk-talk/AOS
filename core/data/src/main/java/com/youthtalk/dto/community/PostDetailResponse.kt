package com.youthtalk.dto.community

import kotlinx.serialization.Serializable
/**
 * {
 * "postId":41,
 * "postType":"review",
 * "title":"테스트입니다",
 * "contentList":[{"content":"아아아앙아아아아아ㅏ 너무 힘들다","type":"TEXT"},{"content":"https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/d9b44863-649d-4a8b-8b59-e46f5fa2d10e-JPEG_1747479535591_5306661591975405272.jpg","type":"IMAGE"},{"content":"","type":"TEXT"}],
 * "policyId":15,
 * "policyTitle":"청년문화예술기획자 양성학교 운영",
 * "writerId":17,
 * "nickname":"윤찬",
 * "view":0,
 * "images":["https://youthtalktalk.s3.ap-northeast-2.amazonaws.com/d9b44863-649d-4a8b-8b59-e46f5fa2d10e-JPEG_1747479535591_5306661591975405272.jpg"],
 * "category":"004",
 * "scrap":false}
 * }
 * */
@Serializable
data class PostDetailResponse(
    val postId: Long,
    val postType: String,
    val title: String,
    val contentList: List<PostContentInfoResponse>,
    val policyId: Long?,
    val policyTitle: String?,
    val writerId: Long,
    val nickname: String?,
    val view: Long,
    val images: List<String>,
    val category: String?,
    val scrap: Boolean
)

@Serializable
data class PostContentInfoResponse(
    val content: String,
    val type: String
)
