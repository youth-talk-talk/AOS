package com.youthtalk.datasource.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.data.PolicyService
import com.youthtalk.mapper.toData
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class HomePagingSource @Inject constructor(
    private val policyService: PolicyService,
    private val dataSource: DataStoreDataSource,
) : PagingSource<Int, Policy>() {

    override fun getRefreshKey(state: PagingState<Int, Policy>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Policy> {
        try {
            val pageNumber = params.key ?: 0
            val getCategories: List<String> = dataSource.getCategoryFilter().first().map { it.name }

            val response = policyService.getPolices(
                categories = getCategories,
                page = pageNumber,
                size = params.loadSize,
            )

            val policies = response.data?.allPolicies?.map { it.toData() } ?: listOf()

            return LoadResult.Page(
                data = policies,
                prevKey = null,
                nextKey = if (policies.size == params.loadSize) pageNumber + 1 else null,
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
