package com.youthtalk.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.youthtalk.datasource.mypage.post.ScrapPostDao
import com.youthtalk.datasource.mypage.post.ScrapPostRemoteKey
import com.youthtalk.datasource.mypage.post.ScrapPostRemoteKeyDao
import com.youthtalk.datasource.post.PostDao
import com.youthtalk.datasource.post.PostRemoteKey
import com.youthtalk.datasource.post.PostRemoteKeyDao
import com.youthtalk.datasource.review.ReviewPostDao
import com.youthtalk.datasource.review.ReviewPostRemoteKey
import com.youthtalk.datasource.review.ReviewPostRemoteKeyDao
import com.youthtalk.model.Post
import com.youthtalk.model.ReviewPost
import com.youthtalk.model.ScrapPost

@Database(
    entities = [
        Post::class,
        PostRemoteKey::class,
        ReviewPost::class,
        ReviewPostRemoteKey::class,
        ScrapPost::class,
        ScrapPostRemoteKey::class
    ],
    version = 1
)
abstract class YouthDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun reviewPostDao(): ReviewPostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
    abstract fun reviewPostRemoteKeyDao(): ReviewPostRemoteKeyDao
    abstract fun scrapPostDao(): ScrapPostDao
    abstract fun scrapPostRemoteKeyDao(): ScrapPostRemoteKeyDao
}
