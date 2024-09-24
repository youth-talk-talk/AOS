package com.youthtalk.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.AnnounceRepository
import com.core.exception.NoDataException
import com.youthtalk.data.AnnounceService
import com.youthtalk.datasource.announce.AnnouncePagingSource
import com.youthtalk.dto.mypage.AnnounceDetailResponse
import com.youthtalk.mapper.toData
import com.youthtalk.model.Announce
import com.youthtalk.model.AnnounceDetail
import com.youthtalk.utils.ErrorUtils.throwableError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnnounceRepositoryImpl @Inject constructor(
    private val announceService: AnnounceService,
) : AnnounceRepository {

    override fun getAnnounces(): Flow<PagingData<Announce>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
            ),
            pagingSourceFactory = {
                AnnouncePagingSource(announceService)
            },
        ).flow
    }

    override fun getAnnounceDetail(id: Long): Flow<AnnounceDetail> = flow {
        runCatching {
            announceService.getAnnounceDetail(id)
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.toData())
                } ?: throw NoDataException()
            }
            .onFailure {
                throwableError<AnnounceDetailResponse>(it)
            }
    }
}
