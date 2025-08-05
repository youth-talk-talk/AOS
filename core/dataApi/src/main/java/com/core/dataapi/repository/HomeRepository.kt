package com.core.dataapi.repository

import com.youthtalk.model.home.HomeData
import com.youthtalk.model.home.NewPolicies
import com.youthtalk.model.typeenum.SortType

interface HomeRepository {
    suspend fun getHome(): Result<HomeData>
    suspend fun getNewPolicies(sortType: SortType): Result<NewPolicies>
}
