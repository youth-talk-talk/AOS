package com.youthtalk.datasource.policy

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyType

@Dao
interface PolicyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Policy>)

    @Query("SELECT * FROM policy where policyType=:policyType")
    fun getPagingSource(policyType: PolicyType): PagingSource<Int, Policy>

    @Query("DELETE FROM policy where policyType=:policyType")
    fun deleteAll(policyType: PolicyType)

    @Query("DELETE FROM policy WHERE policyId=:policyId")
    suspend fun deletePolicy(policyId: Long)

    @Query(
        """
        UPDATE policy
            SET
                scrap = :scrap,
                scrapCount = CASE
                    WHEN scrap = 0 THEN scrapCount + 1
                    ELSE scrapCount - 1
                END
            WHERE policyId = :policyId;
    """
    )
    suspend fun updatePostScrap(policyId: Long, scrap: Boolean)

    @Query("SELECT * FROM policy where policyType=:policyType AND scrap=1")
    fun getScrapPagingSource(policyType: PolicyType): PagingSource<Int, Policy>
}
