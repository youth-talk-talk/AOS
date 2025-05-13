package com.youthtalk.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.youthtalk.datasource.mypage.post.ScrapPostDao
import com.youthtalk.datasource.mypage.post.ScrapPostRemoteKey
import com.youthtalk.datasource.mypage.post.ScrapPostRemoteKeyDao
import com.youthtalk.datasource.policy.PolicyDao
import com.youthtalk.datasource.policy.PolicyRemoteKey
import com.youthtalk.datasource.policy.PolicyRemoteKeyDao
import com.youthtalk.datasource.post.PostDao
import com.youthtalk.datasource.post.PostRemoteKey
import com.youthtalk.datasource.post.PostRemoteKeyDao
import com.youthtalk.datasource.review.ReviewPostDao
import com.youthtalk.datasource.review.ReviewPostRemoteKey
import com.youthtalk.datasource.review.ReviewPostRemoteKeyDao
import com.youthtalk.model.Post
import com.youthtalk.model.ReviewPost
import com.youthtalk.model.ScrapPost
import com.youthtalk.model.policy.Policy

@Database(
    entities = [
        Post::class,
        PostRemoteKey::class,
        ReviewPost::class,
        ReviewPostRemoteKey::class,
        ScrapPost::class,
        ScrapPostRemoteKey::class,
        Policy::class,
        PolicyRemoteKey::class
    ],
    version = 1,
    exportSchema = true
)
abstract class YouthDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun reviewPostDao(): ReviewPostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
    abstract fun reviewPostRemoteKeyDao(): ReviewPostRemoteKeyDao
    abstract fun scrapPostDao(): ScrapPostDao
    abstract fun scrapPostRemoteKeyDao(): ScrapPostRemoteKeyDao
    abstract fun policyRemoteKeyDao(): PolicyRemoteKeyDao
    abstract fun policyDao(): PolicyDao
}
