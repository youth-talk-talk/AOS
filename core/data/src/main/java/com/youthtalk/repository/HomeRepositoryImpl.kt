package com.youthtalk.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.HomeRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.InvalidValueException
import com.core.exception.NetworkErrorException
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.home.HomePagingSource
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.SignErrorResponse
import com.youthtalk.mapper.toData
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import retrofit2.HttpException
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
                } ?: throw NoDataException("not response Data")
            }
            .onFailure { error ->
                Log.d("YOON-CHAN", "getTop5Polices ${error.message}")
                when (error) {
                    is HttpException -> {
                        val e = Json.decodeFromString<CommonResponse<SignErrorResponse>>(
                            error.response()?.errorBody()?.string() ?: "",
                        )

                        when (error.code()) {
                            400 -> throw InvalidValueException("${e.status} ${e.data?.messages?.toString()}")
                        }
                    }

                    else -> throw NetworkErrorException("network Error")
                }
            }
    }

    override fun getHomePolicyMap(): Flow<Map<String, Boolean>> = flow {
        emit(homePolicyMap)
    }

    override fun postHomePolicyScrap(id: String, scrap: Boolean): Flow<Map<String, Boolean>> = flow {
        Log.d("YOON-CHAN", "postHomePolicyScrap $id $scrap")
        homePolicyMap = if (homePolicyMap.containsKey(id)) {
            homePolicyMap - id
            homePolicyMap + Pair(id, !scrap)
        } else {
            homePolicyMap + Pair(id, !scrap)
        }
        Log.d("YOON-CHAN", "postHomePolicyScrap homePOlicyMap $homePolicyMap")
        emit(homePolicyMap)
    }
}
