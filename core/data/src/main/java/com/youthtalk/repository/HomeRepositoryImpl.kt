package com.youthtalk.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.HomeRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.home.HomePagingSource
import com.youthtalk.dto.HomePoliciesResponse
import com.youthtalk.mapper.toData
import com.youthtalk.model.Policy
import com.youthtalk.utils.ErrorUtils.throwableError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val dataSource: DataStoreDataSource,
) : HomeRepository {
    companion object {
        private var homePolicyMap: Map<String, Boolean> = mapOf()
    }

    override fun getPolices(): Flow<PagingData<Policy>> {
        homePolicyMap = mapOf()
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
            ),
            pagingSourceFactory = {
                HomePagingSource(
                    policyService = policyService,
                    dataSource = dataSource,
                )
            },
        ).flow
    }

    override fun getTop5Polices(): Flow<List<Policy>> = flow {
        val categories = dataSource.getCategoryFilter().first().map { it.name }
        runCatching {
            policyService.getPolices(categories, 0, 0)
        }
            .onSuccess { response ->
                response.data?.let { homePolices ->
                    emit(homePolices.top5Policies.map { it.toData() })
                } ?: throw NoDataException()
            }
            .onFailure { error ->
                throwableError<HomePoliciesResponse>(error)
            }
    }

    override fun getHomePolicyMap(): Flow<Map<String, Boolean>> = flow {
        emit(homePolicyMap)
    }

    override fun postHomePolicyScrap(id: String, scrap: Boolean): Flow<Map<String, Boolean>> = flow {
        homePolicyMap = if (homePolicyMap.containsKey(id)) {
            homePolicyMap - id
            homePolicyMap + Pair(id, !scrap)
        } else {
            homePolicyMap + Pair(id, !scrap)
        }
        emit(homePolicyMap)
    }
}
