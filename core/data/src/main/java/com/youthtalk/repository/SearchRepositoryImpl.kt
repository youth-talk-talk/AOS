package com.youthtalk.repository

import com.core.dataapi.repository.SearchRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.data.CommunityService
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.PagingSize
import com.youthtalk.dto.PostSearchResponse
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val dataSource: DataStoreDataSource,
    private val communityService: CommunityService
) : SearchRepository {
    override fun getRecentList(): Flow<List<String>> = dataSource.getRecentSearchList()

    override suspend fun postRecentList(recentList: List<String>) {
        dataSource.setRecentList(recentList)
    }

    override fun getPostsCount(type: String, keyword: String): Flow<Int> = flow {
        runCatching {
            communityService.getSearchPosts(
                keyword = keyword,
                type = type,
                page = 0,
                size = PagingSize.SEARCH_PAGE_SIZE
            )
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.total)
                }
            }
            .onFailure {
                throwableError<PostSearchResponse>(it)
            }
    }
}
