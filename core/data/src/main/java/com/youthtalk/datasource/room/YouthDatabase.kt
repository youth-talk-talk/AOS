package com.youthtalk.datasource.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youthtalk.datasource.policy.PolicyDao
import com.youthtalk.datasource.policy.PolicyRemoteKey
import com.youthtalk.datasource.policy.PolicyRemoteKeyDao
import com.youthtalk.datasource.post.PostDao
import com.youthtalk.datasource.post.PostRemoteKey
import com.youthtalk.datasource.post.PostRemoteKeyDao
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.post.Post

@Database(
    entities = [
        Post::class,
        PostRemoteKey::class,
        Policy::class,
        PolicyRemoteKey::class
    ],
    version = 2,
    autoMigrations = [ AutoMigration(1, 2) ],
    exportSchema = true
)
@TypeConverters(value = [DateTimeConverter::class])
abstract class YouthDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
    abstract fun policyRemoteKeyDao(): PolicyRemoteKeyDao
    abstract fun policyDao(): PolicyDao
}
