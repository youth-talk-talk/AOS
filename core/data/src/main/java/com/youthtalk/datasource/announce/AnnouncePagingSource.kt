package com.youthtalk.datasource.announce

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.youthtalk.data.AnnounceService
import com.youthtalk.mapper.toData
import com.youthtalk.model.Announce
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AnnouncePagingSource @Inject constructor(
    private val announceService: AnnounceService,
) : PagingSource<Int, Announce>() {

    override fun getRefreshKey(state: PagingState<Int, Announce>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val close = state.closestPageToPosition(anchorPosition)
            close?.prevKey?.plus(1) ?: close?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Announce> {
        try {
            val pageNumber = params.key ?: 0

            val response = announceService.getAllAnnounce(
                page = pageNumber,
                size = params.loadSize,
            )

            val data = response.data?.announcementList?.map { it.toData() } ?: listOf()

            return LoadResult.Page(
                data = data,
                prevKey = if (pageNumber == 0) null else pageNumber - 1,
                nextKey = if (response.data?.totalPage == pageNumber) null else pageNumber + 1,
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
