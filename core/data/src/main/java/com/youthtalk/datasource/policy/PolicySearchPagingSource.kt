package com.youthtalk.datasource.policy

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.youthtalk.data.PolicyService
import com.youthtalk.model.policy.SearchPolicy
import java.io.IOException
import javax.inject.Inject
import okhttp3.RequestBody
import retrofit2.HttpException

class PolicySearchPagingSource @Inject constructor(
    private val policyService: PolicyService,
    private val requestBody: RequestBody
) : PagingSource<Int, SearchPolicy>() {

    override fun getRefreshKey(state: PagingState<Int, SearchPolicy>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val close = state.closestPageToPosition(anchorPosition)
            close?.prevKey?.plus(1) ?: close?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchPolicy> {
        try {
            val pageNumber = params.key ?: 0

            val response = policyService.postSpecPolicies(
                requestBody = requestBody,
                page = pageNumber,
                size = params.loadSize
            )

            val data = response.data?.policyList?.map { SearchPolicy(it.policyId, it.title) } ?: listOf()

            return LoadResult.Page(
                data = data,
                prevKey = if (pageNumber == 0) null else pageNumber - 1,
                nextKey = if ((response.data?.totalCount ?: 0) / 10 == pageNumber) null else pageNumber + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
