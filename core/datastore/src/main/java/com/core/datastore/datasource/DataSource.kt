package com.core.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun hasToken(): Flow<Boolean>
}
