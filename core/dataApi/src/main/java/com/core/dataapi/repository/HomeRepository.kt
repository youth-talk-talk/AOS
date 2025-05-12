package com.core.dataapi.repository

import com.youthtalk.model.home.HomeData
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHome(): Flow<HomeData>
}
