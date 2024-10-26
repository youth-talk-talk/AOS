package com.youthtalk.datasource.post

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: PostRemoteKey)

    @Query("SELECT * FROM postremotekey ORDER BY nextPage DESC LIMIT 1")
    suspend fun getNextKey(): PostRemoteKey

    @Query("DELETE FROM postremotekey")
    suspend fun deleteAll()
}
