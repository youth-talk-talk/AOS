package com.youthtalk.datasource.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.PagingSize
import com.youthtalk.mapper.toData
import com.youthtalk.model.SearchPolicy
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchPoliciesTitlePagingSource @Inject constructor(
    private val title: String,
    private val policyService: PolicyService,
) : PagingSource<Int, SearchPolicy>() {

    override fun getRefreshKey(state: PagingState<Int, SearchPolicy>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            return anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchPolicy> {
        try {
            val page = params.key ?: 0
            val response = policyService.getSearchPoliciesTitle(
                title = title,
                page = page,
                size = params.loadSize,
            )

            val data = response.data?.map { it.toData() } ?: listOf()

            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (data.isEmpty()) null else page + (params.loadSize / PagingSize.SEARCH_PAGE_SIZE),
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
