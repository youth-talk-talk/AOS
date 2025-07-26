package com.core.dataapi.repository

import com.youthtalk.model.User
import com.youthtalk.model.comment.SettingCommentInfo
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(): Result<User>
    suspend fun postUser(nickname: String, region: Region): Result<User>
    suspend fun deleteUser(deleteUser: Boolean): Result<Long>
    suspend fun blockUser(userId: Long): Result<Unit>
    fun getCategoryList(): Flow<List<Category>>
    fun getReviewCategoryList(): Flow<List<Category>>

    fun getLikeComments(isLike: Boolean): Flow<SettingCommentInfo>

    suspend fun setCategoryList(categories: List<Category>)
    suspend fun setReviewCategoryList(categories: List<Category>)
}
