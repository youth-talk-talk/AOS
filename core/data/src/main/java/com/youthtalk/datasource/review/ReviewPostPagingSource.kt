package com.youthtalk.datasource.review

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.data.CommunityService
import com.youthtalk.mapper.toData
import com.youthtalk.model.ReviewPost
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ReviewPostPagingSource @Inject constructor(
    private val communityService: CommunityService,
    private val dataSource: DataStoreDataSource,
) : PagingSource<Int, ReviewPost>() {
    override fun getRefreshKey(state: PagingState<Int, ReviewPost>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewPost> {
        try {
            val pageNumber = params.key ?: 0
            val categories = dataSource.getReviewCategoryFilter().first().map { it.name }

            val response = communityService.postReviewPosts(
                categories = categories,
                page = pageNumber,
                size = params.loadSize,
            )

            val reviewPosts = response.data?.reviewPosts?.map { it.toData() } ?: listOf()
            return LoadResult.Page(
                data = reviewPosts,
                prevKey = null,
                nextKey = if (reviewPosts.size == params.loadSize) pageNumber + 1 else null,
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
