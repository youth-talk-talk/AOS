package com.core.dataapi.repository

import com.youthtalk.model.home.HomeData
import com.youthtalk.model.home.NewPolicies
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHome(): Flow<HomeData>
    fun getNewPolicies(): Flow<NewPolicies>
}
