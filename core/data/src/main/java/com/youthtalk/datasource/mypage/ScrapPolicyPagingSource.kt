package com.youthtalk.datasource.mypage

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.PagingSize.SCRAP_PAGE_SIZE
import com.youthtalk.mapper.toData
import com.youthtalk.model.Policy
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ScrapPolicyPagingSource @Inject constructor(
    private val policyService: PolicyService,
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

            val response = policyService.getScrapPolicies(pageNumber, params.loadSize)
            val policies = response.data?.map { it.toData() } ?: listOf()

            return LoadResult.Page(
                data = policies,
                prevKey = if (pageNumber == 0) null else pageNumber - 1,
                nextKey = if (policies.isEmpty()) null else pageNumber + (params.loadSize / SCRAP_PAGE_SIZE),
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
