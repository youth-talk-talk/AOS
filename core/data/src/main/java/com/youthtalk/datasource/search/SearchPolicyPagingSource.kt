package com.youthtalk.datasource.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.youthtalk.data.PolicyService
import com.youthtalk.datasource.PagingSize.SEARCH_PAGE_SIZE
import com.youthtalk.dto.specpolicy.FilterInfoRequest
import com.youthtalk.mapper.toData
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchPolicyPagingSource @Inject constructor(
    private val policyService: PolicyService,
    private val filterInfo: FilterInfo,
    private val keyword: String? = null,
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
            val requestBody = FilterInfoRequest(
                age = filterInfo.age,
                categories = null,
                employmentCodeList = filterInfo.employmentCodeList,
                keyword = keyword,
                isFinished = filterInfo.isFinished,
            ).toRequestBody()

            val response = policyService.postSpecPolicies(
                requestBody = requestBody,
                page = pageNumber,
                size = params.loadSize,
            )

            val policies = response.data?.policyList?.map { it.toData() } ?: listOf()

            return LoadResult.Page(
                data = policies,
                prevKey = null,
                nextKey = if (policies.isEmpty()) null else pageNumber + (params.loadSize / SEARCH_PAGE_SIZE),
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
