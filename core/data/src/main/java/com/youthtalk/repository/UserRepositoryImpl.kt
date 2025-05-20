package com.youthtalk.repository

import com.core.dataapi.repository.UserRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.data.UserService
import com.youthtalk.dto.UserRequest
import com.youthtalk.dto.UserResponse
import com.youthtalk.mapper.toData
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val dataSource: DataStoreDataSource
) : UserRepository {

    override fun getUser(): Flow<User> = flow {
        runCatching { userService.getUser() }
            .onSuccess { response ->
                response.data?.let { userResponse ->
                    emit(userResponse.toData())
                }
            }
            .onFailure {
                throwableError<UserResponse>(it)
            }
    }

    override fun postUser(nickname: String, region: Region, imageUrl: String?): Flow<User> = flow {
        runCatching { userService.postUser(UserRequest(nickname, region.region, imageUrl).toRequestBody()) }
            .onSuccess { response ->
                response.data?.let { userResponse ->
                    emit(userResponse.toData())
                }
            }
            .onFailure {
                throwableError<UserResponse>(it)
            }
    }

    override fun deleteUser(deleteUser: Boolean): Flow<Long> = flow {
        dataSource.clearData()
        if (deleteUser) {
            runCatching { userService.postDeleteUser() }
                .onSuccess {
                    emit(0L)
                }
                .onFailure {
                    throwableError<UserResponse>(it)
                }
        } else {
            emit(0L)
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
}
