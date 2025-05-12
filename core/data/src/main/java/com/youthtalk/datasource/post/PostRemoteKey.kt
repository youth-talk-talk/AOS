package com.youthtalk.datasource.post

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostRemoteKey(
    @PrimaryKey val nextPage: Int
)
