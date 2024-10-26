package com.youthtalk.datasource.review

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReviewPostRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: ReviewPostRemoteKey)

    @Query("SELECT * FROM reviewpostremotekey ORDER BY nextPage DESC LIMIT 1")
    suspend fun getNextKey(): ReviewPostRemoteKey

    @Query("DELETE FROM reviewpostremotekey")
    suspend fun deleteAll()
}
