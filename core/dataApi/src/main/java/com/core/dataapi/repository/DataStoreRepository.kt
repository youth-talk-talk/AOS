package com.core.dataapi.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun hasToken(): Flow<Boolean>
}
