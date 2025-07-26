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
    fun postScrap(id: Long, scrap: Boolean): Flow<String>
    fun searchPolicyName(policyName: String): Flow<PagingData<SearchPolicy>>
    fun postAddComment(policyId: Long, text: String): Flow<Long>
    fun postDeleteComment(commentId: Long): Flow<String>
    fun getScrapPolicies(): Flow<Flow<PagingData<Policy>>>
    fun deleteAllRecentlyViewPolicies(): Flow<String>
}
