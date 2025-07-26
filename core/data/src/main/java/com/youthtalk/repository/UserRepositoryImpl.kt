package com.youthtalk.repository

import com.core.dataapi.repository.UserRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.UserService
import com.youthtalk.dto.UserRequest
import com.youthtalk.mapper.toData
import com.youthtalk.model.User
import com.youthtalk.model.comment.SettingCommentInfo
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import com.youthtalk.utils.ErrorUtils.createResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val dataSource: DataStoreDataSource
) : UserRepository {

    override suspend fun getUser(): Result<User> = createResult {
        userService.getUser().data?.toData() ?: throw NoDataException()
    }

    override suspend fun postUser(nickname: String, region: Region): Result<User> = createResult {
        userService.postUser(UserRequest(nickname, region.region).toRequestBody()).data?.toData() ?: throw NoDataException()
    }

    override suspend fun deleteUser(deleteUser: Boolean): Result<Long> = createResult {
        dataSource.clearData()
        userService.postDeleteUser()
        0L
    }

    override suspend fun blockUser(userId: Long): Result<Unit> {
        return createResult {
            userService.blockUser(userId)
        }
    }

    override fun getCategoryList(): Flow<List<Category>> = dataSource.getCategoryFilter()

    override suspend fun setCategoryList(categories: List<Category>) {
        dataSource.setCategoryFilter(categories)
    }

    override fun getReviewCategoryList(): Flow<List<Category>> {
        return dataSource.getReviewCategoryFilter()
    }

    override suspend fun setReviewCategoryList(categories: List<Category>) {
        dataSource.setReviewCategoryFilter(categories)
    }

    override suspend fun getLikeComments(isLike: Boolean): Result<SettingCommentInfo> = createResult {
        if (isLike) {
            userService.getLikeComments()
        } else {
            userService.getMyComments()
        }.data?.toData() ?: throw NoDataException()
    }
}
