package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.policy.SearchPolicy
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.SortType
import kotlinx.coroutines.flow.Flow

interface SpecPolicyRepository {
    fun getPolicies(searchFilter: SearchFilter, policyType: PolicyType, sortType: SortType): Flow<PagingData<Policy>>
    suspend fun getCount(searchFilter: SearchFilter, sortType: SortType): Result<Int>
    suspend fun postScrap(id: Long, scrap: Boolean): Result<String>
    fun searchPolicyName(policyName: String): Flow<PagingData<SearchPolicy>>
    suspend fun postAddComment(policyId: Long, text: String): Result<Long>
    suspend fun postDeleteComment(commentId: Long): Result<String>
    fun getScrapPolicies(): Flow<PagingData<Policy>>
    suspend fun deleteAllRecentlyViewPolicies(): Result<String>
}
