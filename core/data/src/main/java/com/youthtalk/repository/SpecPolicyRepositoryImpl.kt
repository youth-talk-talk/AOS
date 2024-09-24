package com.youthtalk.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.SpecPolicyRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.CommentService
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.specpolicy.SpecPolicyPagingSource
import com.youthtalk.dto.PostAddCommentResponse
import com.youthtalk.dto.specpolicy.CommentRequest
import com.youthtalk.dto.specpolicy.FilterInfoRequest
import com.youthtalk.dto.specpolicy.SpecPoliciesResponse
import com.youthtalk.model.Category
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import com.youthtalk.utils.ErrorUtils.throwableError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SpecPolicyRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val commentService: CommentService,
    private val dataSource: DataStoreDataSource,
) : SpecPolicyRepository {
    override fun getPolicies(categories: List<Category>?, keyword: String?): Flow<Flow<PagingData<Policy>>> = flow {
        emit(
            Pager(
                pagingSourceFactory = {
                    SpecPolicyPagingSource(
                        policyService = policyService,
                        dataSource = dataSource,
                        category = categories,
                        keyword = keyword,
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

    override fun getCount(categories: List<Category>?, keyword: String?): Flow<Int> = flow {
        val requestBody = FilterInfoRequest(
            age = dataSource.getAge().first(),
            categories = categories,
            employmentCodeList = dataSource.getEmployCode().first(),
            keyword = keyword,
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
                } ?: throw NoDataException()
            }
            .onFailure {
                throwableError<SpecPoliciesResponse>(it)
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
                throwableError<Unit>(it)
            }
    }

    override fun postAddComment(policyId: String, text: String): Flow<Long> = flow {
        runCatching {
            policyService.postAddComment(
                CommentRequest(policyId, text).toRequestBody(),
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
