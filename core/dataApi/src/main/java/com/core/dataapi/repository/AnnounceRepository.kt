package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.Announce
import com.youthtalk.model.AnnounceDetail
import kotlinx.coroutines.flow.Flow

interface AnnounceRepository {
    fun getAnnounces(): Flow<PagingData<Announce>>
    fun getAnnounceDetail(id: Long): Flow<AnnounceDetail>
}
