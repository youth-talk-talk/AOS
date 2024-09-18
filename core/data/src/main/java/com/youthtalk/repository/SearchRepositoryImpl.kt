package com.youthtalk.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.SearchRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.CommunityService
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.PagingSize
import com.youthtalk.datasource.search.SearchPoliciesTitlePagingSource
import com.youthtalk.datasource.search.SearchPolicyPagingSource
import com.youthtalk.datasource.search.SearchPostPagingSource
import com.youthtalk.dto.specpolicy.FilterInfoRequest
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import com.youthtalk.model.Post
import com.youthtalk.model.SearchPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val dataSource: DataStoreDataSource,
    private val communityService: CommunityService,
) : SearchRepository {
    override fun getRecentList(): Flow<List<String>> = dataSource.getRecentSearchList()

    override fun getPolicies(filterInfo: FilterInfo, keyword: String): Flow<Flow<PagingData<Policy>>> = flow {
        emit(
            Pager(
                pagingSourceFactory = {
                    SearchPolicyPagingSource(
                        policyService = policyService,
                        filterInfo = filterInfo,
                        keyword = keyword,
                    )
                },
                config = PagingConfig(
                    pageSize = PagingSize.SEARCH_PAGE_SIZE,
                    initialLoadSize = PagingSize.SEARCH_PAGE_SIZE,
                ),
            ).flow,
        )
    }

    override fun getPoliciesCount(filterInfo: FilterInfo, keyword: String): Flow<Int> = flow {
        val requestBody = FilterInfoRequest(
            age = filterInfo.age,
            categories = null,
            employmentCodeList = filterInfo.employmentCodeList,
            keyword = keyword,
            isFinished = filterInfo.isFinished,
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

    override suspend fun postRecentList(recentList: List<String>) {
        dataSource.setRecentList(recentList)
    }

    override fun getPosts(type: String, keyword: String): Flow<Flow<PagingData<Post>>> = flow {
        emit(
            Pager(
                pagingSourceFactory = {
                    SearchPostPagingSource(
                        communityService = communityService,
                        type = type,
                        keyword = keyword,
                    )
                },
                config = PagingConfig(
                    pageSize = PagingSize.SEARCH_PAGE_SIZE,
                    initialLoadSize = PagingSize.SEARCH_PAGE_SIZE,
                ),
            ).flow,
        )
    }

    override fun getPostsCount(type: String, keyword: String): Flow<Int> = flow {
        runCatching {
            communityService.getSearchPosts(
                keyword = keyword,
                type = type,
                page = 0,
                size = PagingSize.SEARCH_PAGE_SIZE,
            )
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.total)
                }
            }
            .onFailure {
                Log.d("YOON-CHAN", "SpecPolicy getPostsCount error ${it.message}")
            }
    }

    override fun getSearchPolicies(title: String): Flow<PagingData<SearchPolicy>> {
        return Pager(
            pagingSourceFactory = {
                SearchPoliciesTitlePagingSource(
                    title = title,
                    policyService = policyService,
                )
            },
            config = PagingConfig(
                pageSize = PagingSize.SEARCH_PAGE_SIZE,
            ),
        ).flow
    }
}
