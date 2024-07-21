package com.core.dataapi.repository

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun postLogin(userId: String): Flow<Long>

    fun hasToken(): Flow<Boolean>

    fun postSign(id: String, nickname: String, region: String): Flow<Int>
}
