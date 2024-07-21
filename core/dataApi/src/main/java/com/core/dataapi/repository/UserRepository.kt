package com.core.dataapi.repository

import com.youthtalk.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User>
}
