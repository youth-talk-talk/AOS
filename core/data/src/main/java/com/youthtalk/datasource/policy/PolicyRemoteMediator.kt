package com.youthtalk.datasource.policy

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.typeenum.SortType
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.delay
import okhttp3.RequestBody
import retrofit2.HttpException
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class PolicyRemoteMediator @Inject constructor(
    private val policyService: PolicyService,
    private val requestBody: RequestBody,
    private val policyType: PolicyType,
    private val sortType: SortType,
    private val youthDatabase: YouthDatabase
) : RemoteMediator<Int, Policy>() {
    private val policyDao = youthDatabase.policyDao()
    private val policyRemoteKeyDao = youthDatabase.policyRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Policy>): MediatorResult {
        val remoteKey = when (loadType) {
            LoadType.REFRESH -> {
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(true)
            }
            LoadType.APPEND -> {
                policyRemoteKeyDao.getNextKey(policyType)
            }
        }
        try {
            youthDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    policyDao.deleteAll(policyType)
                    policyRemoteKeyDao.deleteAll(policyType)
                }
                delay(300L)
            }
            val page = remoteKey?.nextPage ?: 0
            val response = policyService.postSpecPolicies(
                requestBody = requestBody,
                sort = sortType,
                page = page,
                size = state.config.pageSize
            )
            val totalCount = response.data?.totalCount ?: 0
            val policies = response.data?.policyList?.map { it.toDomain().copy(policyType = policyType) } ?: listOf()
            Timber.e("PolicyRemoteMediator page $page, policies ${policies.map { it.title }}")
            youthDatabase.withTransaction {
                policyRemoteKeyDao.insertOrReplace(PolicyRemoteKey(nextPage = page + 1, policyType = policyType))
                policyDao.insertAll(policies)
            }
            return MediatorResult.Success(endOfPaginationReached = page == (totalCount / state.config.pageSize))
        } catch (e: HttpException) {
            Timber.e("PolicyRemoteMediator error HttpException $e")
            return MediatorResult.Error(e)
        } catch (e: IOException) {
            Timber.e("PolicyRemoteMediator error IOException $e")
            return MediatorResult.Error(e)
        }
    }
}
