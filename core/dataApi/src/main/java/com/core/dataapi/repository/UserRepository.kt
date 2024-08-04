package com.core.dataapi.repository

import com.youthtalk.model.Category
import com.youthtalk.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User>

    fun getCategoryList(): Flow<List<Category>>

    suspend fun setCategoryList(categories: List<Category>)
}
