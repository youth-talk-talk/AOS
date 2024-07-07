package com.core.dataapi.repository

import com.youthtalk.model.Token
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun postLogin(userId: String): Flow<Token>
}
