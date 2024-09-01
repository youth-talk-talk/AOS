package com.youthtalk.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.SpecPolicyRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.specpolicy.SpecPolicyPagingSource
import com.youthtalk.dto.specpolicy.FilterInfoRequest
import com.youthtalk.model.Category
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SpecPolicyRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val dataSource: DataStoreDataSource,
) : SpecPolicyRepository {
    override fun getPolicies(categories: List<Category>): Flow<Flow<PagingData<Policy>>> = flow {
        emit(
            Pager(
                pagingSourceFactory = {
                    SpecPolicyPagingSource(
                        policyService = policyService,
                        dataSource = dataSource,
                        category = categories,
                    )
                },
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 10,
                    enablePlaceholders = true,
                ),
            ).flow,
        )
    }

    override fun getCount(categories: List<Category>): Flow<Int> = flow {
        val requestBody = FilterInfoRequest(
            age = dataSource.getAge().first(),
            categories = categories,
            employmentCodeList = dataSource.getEmployCode().first(),
            keyword = null,
            isFinished = dataSource.getFinish().first(),
        ).toRequestBody()

        runCatching {
            policyService.postSpecPolicies(
                requestBody = requestBody,
                page = 0,
                size = 10,
            )
        }
            .onSuccess { response ->
                response.data?.let { specPolicyInfo ->
                    emit(specPolicyInfo.totalCount)
                } ?: throw NoDataException("not response Data")
            }
            .onFailure {
                Log.d("YOON-CHAN", "SpecPolicy error ${it.message}")
            }
    }

    override fun getFilterInfo(): Flow<FilterInfo> = combine(
        dataSource.getAge(),
        dataSource.getEmployCode(),
        dataSource.getFinish(),
    ) { age, employCode, isFinished ->
        FilterInfo(age, employCode, isFinished)
    }

    override fun saveFilterInfo(filterInfo: FilterInfo): Flow<FilterInfo> = flow {
        dataSource.setAge(filterInfo.age)
        dataSource.setFinish(filterInfo.isFinished)
        dataSource.setEmployCodeFilter(filterInfo.employmentCodeList)
        emit(filterInfo)
    }

    override fun postScrap(id: String): Flow<String> = flow {
        runCatching {
            policyService.postPolicyScrap(id)
        }
            .onSuccess { response ->
                emit(response.message)
            }
            .onFailure {
                Log.d("YOON-CHAN", "postScrap error ${it.message}")
            }
    }
}
