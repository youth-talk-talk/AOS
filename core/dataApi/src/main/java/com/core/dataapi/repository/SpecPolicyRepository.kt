package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.search.SearchFilter
import kotlinx.coroutines.flow.Flow

interface SpecPolicyRepository {
    fun getPolicies(searchFilter: SearchFilter, policyType: PolicyType): Flow<Flow<PagingData<Policy>>>
    fun getCount(searchFilter: SearchFilter): Flow<Int>
    fun getFilterInfo(): Flow<FilterInfo>
    fun saveFilterInfo(filterInfo: FilterInfo): Flow<FilterInfo>
    fun postScrap(id: String): Flow<String>
    fun postAddComment(policyId: String, text: String): Flow<Long>
    fun postDeleteComment(commentId: Long): Flow<String>
}
