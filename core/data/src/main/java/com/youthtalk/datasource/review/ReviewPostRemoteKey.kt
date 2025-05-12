package com.youthtalk.datasource.review

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReviewPostRemoteKey(
    @PrimaryKey val nextPage: Int
)
