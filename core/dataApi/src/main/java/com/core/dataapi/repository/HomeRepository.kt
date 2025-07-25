package com.core.dataapi.repository

import com.youthtalk.model.home.HomeData
import com.youthtalk.model.home.NewPolicies
import com.youthtalk.model.typeenum.SortType
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getHome(): Result<HomeData>
    fun getNewPolicies(sortType: SortType): Flow<NewPolicies>
}
