package com.youthtalk.datasource.policy

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.youthtalk.model.policy.PolicyType

@Dao
interface PolicyRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: PolicyRemoteKey)

    @Query("SELECT * FROM policyremotekey where policyType=:policyType ORDER BY nextPage DESC LIMIT 1")
    suspend fun getNextKey(policyType: PolicyType): PolicyRemoteKey

    @Query("DELETE FROM policyremotekey where policyType=:policyType")
    suspend fun deleteAll(policyType: PolicyType)
}
