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
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.dto.PostAddCommentResponse
import com.youthtalk.dto.specpolicy.CommentRequest
import com.youthtalk.dto.specpolicy.SpecPoliciesResponse
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.search.SearchFilter
import com.youthtalk.model.typeenum.SortType
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SpecPolicyRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val commentService: CommentService,
    private val dataSource: DataStoreDataSource,
    private val youthDatabase: YouthDatabase
) : SpecPolicyRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPolicies(searchFilter: SearchFilter, policyType: PolicyType, sortType: SortType): Flow<Flow<PagingData<Policy>>> = flow {
        emit(
            Pager(
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
        )
    }

    override fun getCount(searchFilter: SearchFilter, sortType: SortType): Flow<Int> = flow {
        runCatching {
            policyService.postSpecPolicies(
                requestBody = searchFilter.toRequestBody(),
                sort = sortType,
                page = 0,
                size = 10
            )
        }
            .onSuccess { response ->
                response.data?.let { specPolicyInfo ->
                    emit(specPolicyInfo.totalCount)
                } ?: throw NoDataException()
            }
            .onFailure {
                throwableError<SpecPoliciesResponse>(it)
            }
    }

    override fun postScrap(id: String): Flow<String> = flow {
        runCatching {
            policyService.postPolicyScrap(id)
        }
            .onSuccess { response ->
                emit(response.message)
            }
            .onFailure {
                throwableError<Unit>(it)
            }
    }

    override fun postAddComment(policyId: String, text: String): Flow<Long> = flow {
        runCatching {
            policyService.postAddComment(
                CommentRequest(policyId, text).toRequestBody()
            )
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.commentId)
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                throwableError<PostAddCommentResponse>(it)
            }
    }

    override fun postDeleteComment(commentId: Long): Flow<String> = flow {
        runCatching {
            commentService.postDeleteComment(commentId)
        }
            .onSuccess { response ->
                emit(response.message)
            }
            .onFailure {
                throwableError<PostAddCommentResponse>(it)
            }
    }
}
