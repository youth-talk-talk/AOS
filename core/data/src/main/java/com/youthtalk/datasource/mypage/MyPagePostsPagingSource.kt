package com.youthtalk.datasource.mypage

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.PagingSize.MY_PAGE_POSTS_SIZE
import com.youthtalk.mapper.toData
import com.youthtalk.model.Post
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MyPagePostsPagingSource @Inject constructor(
    private val communityService: CommunityService,
    private val type: String,
) : PagingSource<Int, Post>() {
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        try {
            val pageNumber = params.key ?: 0

            val response = communityService.getMyPagePosts(
                type = type,
                page = pageNumber,
                size = MY_PAGE_POSTS_SIZE,
            )

            val data = response.data?.map { it.toData() } ?: listOf()

            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (data.isEmpty()) null else pageNumber + (params.loadSize / MY_PAGE_POSTS_SIZE),
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
