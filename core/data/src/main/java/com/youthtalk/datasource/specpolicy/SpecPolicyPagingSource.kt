package com.youthtalk.datasource.specpolicy

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.data.PolicyService
import com.youthtalk.dto.specpolicy.FilterInfoRequest
import com.youthtalk.mapper.toData
import com.youthtalk.model.Category
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SpecPolicyPagingSource @Inject constructor(
    private val policyService: PolicyService,
    private val dataSource: DataStoreDataSource,
    private val category: List<Category>? = null,
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
                age = dataSource.getAge().first(),
                categories = category,
                employmentCodeList = dataSource.getEmployCode().first(),
                keyword = keyword,
                isFinished = dataSource.getFinish().first(),
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
                nextKey = if (policies.isEmpty()) null else pageNumber + 1,
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
