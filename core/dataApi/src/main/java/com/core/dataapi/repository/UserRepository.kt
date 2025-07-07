package com.core.dataapi.repository

import com.youthtalk.model.User
import com.youthtalk.model.comment.SettingCommentInfo
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User>
    fun postUser(nickname: String, region: Region): Flow<User>
    fun deleteUser(deleteUser: Boolean): Flow<Long>
    suspend fun blockUser(userId: Long): Result<Unit>
    fun getCategoryList(): Flow<List<Category>>
    fun getReviewCategoryList(): Flow<List<Category>>

    fun getLikeComments(isLike: Boolean): Flow<SettingCommentInfo>

    suspend fun setCategoryList(categories: List<Category>)
    suspend fun setReviewCategoryList(categories: List<Category>)
}
