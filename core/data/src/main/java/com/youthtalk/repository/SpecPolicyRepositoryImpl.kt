package com.youthtalk.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.SpecPolicyRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.CommentService
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.policy.PolicyRemoteMediator
import com.youthtalk.datasource.policy.PolicySearchPagingSource
import com.youthtalk.datasource.policy.ScrapPolicyRemoteMediator
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.dto.specpolicy.CommentRequest
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.policy.SearchPolicy
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.SortType
import com.youthtalk.utils.ErrorUtils.createResult
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SpecPolicyRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val commentService: CommentService,
    private val dataSource: DataStoreDataSource,
    private val youthDatabase: YouthDatabase
) : SpecPolicyRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPolicies(searchFilter: SearchFilter, policyType: PolicyType, sortType: SortType): Flow<PagingData<Policy>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            remoteMediator = PolicyRemoteMediator(
                policyService = policyService,
                requestBody = searchFilter.toRequestBody(),
                policyType = policyType,
                sortType = sortType,
                youthDatabase = youthDatabase
            )
        ) {
            youthDatabase.policyDao().getPagingSource(policyType = policyType)
        }.flow
    }

    override fun searchPolicyName(policyName: String): Flow<PagingData<SearchPolicy>> {
        val requestBody = SearchFilter(keyword = policyName).toRequestBody()
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                PolicySearchPagingSource(
                    policyService = policyService,
                    requestBody = requestBody
                )
            }
        ).flow
    }

    override suspend fun getCount(searchFilter: SearchFilter, sortType: SortType): Result<Int> = createResult {
        policyService.postSpecPolicies(
            requestBody = searchFilter.toRequestBody(),
            sort = sortType,
            page = 0,
            size = 10
        ).data?.totalCount ?: throw NoDataException()
    }

    override suspend fun postScrap(id: Long, scrap: Boolean): Result<String> = createResult {
        val response = policyService.postPolicyScrap(id)
        youthDatabase.policyDao().updatePostScrap(id, !scrap)
        response.message
    }

    override suspend fun postAddComment(policyId: Long, text: String): Result<Long> = createResult {
        policyService.postAddComment(
            CommentRequest(policyId, text).toRequestBody()
        ).data?.commentId ?: throw NoDataException()
    }

    override suspend fun postDeleteComment(commentId: Long): Result<String> = createResult {
        commentService.postDeleteComment(commentId).message
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getScrapPolicies(): Flow<PagingData<Policy>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            remoteMediator = ScrapPolicyRemoteMediator(
                policyService = policyService,
                policyType = PolicyType.SCRAP,
                youthDatabase = youthDatabase
            )
        ) {
            youthDatabase.policyDao().getScrapPagingSource(policyType = PolicyType.SCRAP)
        }.flow
    }

    override fun deleteAllRecentlyViewPolicies(): Flow<String> = flow {
        runCatching {
            policyService.deleteAllRecentlyViewPolicies()
        }
            .onSuccess { response ->
                emit(response.data ?: response.message)
            }
            .onFailure {
                Timber.e("deleteAll error $it")
                throwableError<String>(it)
            }
    }
}
