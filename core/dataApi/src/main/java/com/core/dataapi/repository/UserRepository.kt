package com.core.dataapi.repository

import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import java.io.File
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User>
    fun postUser(nickname: String, region: Region): Flow<User>
    fun deleteUser(deleteUser: Boolean): Flow<Long>
    fun getCategoryList(): Flow<List<Category>>
    fun getReviewCategoryList(): Flow<List<Category>>
    fun postUserImage(file: File?): Flow<String>

    suspend fun setCategoryList(categories: List<Category>)
    suspend fun setReviewCategoryList(categories: List<Category>)
}
