package com.youthtalk.model

import com.youthtalk.model.typeenum.Region

data class User(
    val memberId: Long,
    val nickname: String,
    val profileImgUrl: String?,
    val region: Region
)
