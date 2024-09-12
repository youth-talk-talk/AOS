package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.Category
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.Flow

interface SpecPolicyRepository {
    fun getPolicies(categories: List<Category>?, keyword: String?): Flow<Flow<PagingData<Policy>>>
    fun getCount(categories: List<Category>?, keyword: String?): Flow<Int>
    fun getFilterInfo(): Flow<FilterInfo>
    fun saveFilterInfo(filterInfo: FilterInfo): Flow<FilterInfo>
    fun postScrap(id: String): Flow<String>
    fun postAddComment(policyId: String, text: String): Flow<Long>
    fun postDeleteComment(commentId: Long): Flow<String>
}
