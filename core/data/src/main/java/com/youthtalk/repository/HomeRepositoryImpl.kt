package com.youthtalk.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.core.dataapi.repository.HomeRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.InvalidValueException
import com.core.exception.NetworkErrorException
import com.core.exception.NoDataException
import com.youthtalk.data.HomeService
import com.youthtalk.datasource.home.HomePagingSource
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.SignErrorResponse
import com.youthtalk.mapper.toData
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService,
    private val dataSource: DataStoreDataSource,
) : HomeRepository {

    override fun getPolices(): Flow<Flow<PagingData<Policy>>> = flow {
        emit(
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                ),
                pagingSourceFactory = {
                    HomePagingSource(
                        homeService = homeService,
                        dataSource = dataSource,
                    )
                },
            ).flow,
        )
    }

    override fun getTop5Polices(): Flow<List<Policy>> = flow {
        val categories = dataSource.getCategoryFilter().first().map { it.name }
        runCatching {
            homeService.getPolices(categories, 0, 0)
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
}
