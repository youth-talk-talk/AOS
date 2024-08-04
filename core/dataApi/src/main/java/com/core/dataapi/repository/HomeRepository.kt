package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.Policy
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getPolices(): Flow<Flow<PagingData<Policy>>>
    fun getTop5Polices(): Flow<List<Policy>>
}
