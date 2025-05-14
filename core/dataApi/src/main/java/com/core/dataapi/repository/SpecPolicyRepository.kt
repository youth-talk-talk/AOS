package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.SortType
import kotlinx.coroutines.flow.Flow

interface SpecPolicyRepository {
    fun getPolicies(searchFilter: SearchFilter, policyType: PolicyType, sortType: SortType): Flow<Flow<PagingData<Policy>>>
    fun getCount(searchFilter: SearchFilter, sortType: SortType): Flow<Int>
    fun postScrap(id: String): Flow<String>
    fun postAddComment(policyId: String, text: String): Flow<Long>
    fun postDeleteComment(commentId: Long): Flow<String>
}
