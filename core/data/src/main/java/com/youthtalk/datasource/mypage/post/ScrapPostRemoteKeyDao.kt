package com.youthtalk.datasource.mypage.post

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScrapPostRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: ScrapPostRemoteKey)

    @Query("SELECT * FROM scrappostremotekey ORDER BY nextPage DESC LIMIT 1")
    suspend fun getNextKey(): ScrapPostRemoteKey

    @Query("DELETE FROM scrappostremotekey")
    suspend fun deleteAll()
}
