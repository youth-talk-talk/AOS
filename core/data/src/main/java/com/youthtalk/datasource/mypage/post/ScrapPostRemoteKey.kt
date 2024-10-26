package com.youthtalk.datasource.mypage.post

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScrapPostRemoteKey(
    @PrimaryKey val nextPage: Int,
)
