package com.core.dataapi.repository

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun postLogin(socialId: String): Flow<Long>

    fun hasToken(): Flow<Boolean>

    fun postSign(id: String, nickname: String, region: String): Flow<Int>
}
