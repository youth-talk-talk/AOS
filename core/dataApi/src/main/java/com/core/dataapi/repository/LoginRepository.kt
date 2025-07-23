package com.core.dataapi.repository

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun postLogin(socialId: String): Result<Long>

    fun hasToken(): Flow<Boolean>

    suspend fun postSign(id: String, nickname: String, region: String): Result<Int>
}
