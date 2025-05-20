package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.policy.SearchPolicy
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.SortType
import kotlinx.coroutines.flow.Flow

interface SpecPolicyRepository {
    fun getPolicies(searchFilter: SearchFilter, policyType: PolicyType, sortType: SortType): Flow<Flow<PagingData<Policy>>>
    fun getCount(searchFilter: SearchFilter, sortType: SortType): Flow<Int>
    fun postScrap(id: Long, scrap: Boolean): Flow<String>
    fun searchPolicyName(policyName: String): Flow<Flow<PagingData<SearchPolicy>>>
    fun postAddComment(policyId: Long, text: String): Flow<Long>
    fun postDeleteComment(commentId: Long): Flow<String>
}
