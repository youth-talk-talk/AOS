package com.core.datastore.datasource

import com.youthtalk.model.Category
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun hasToken(): Flow<Boolean>
    fun getCategoryFilter(): Flow<List<Category>>
    suspend fun setCategoryFilter(categories: List<Category>)
}
