package com.youthtalk.repository

import android.util.Log
import com.core.dataapi.repository.UserRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.data.UserService
import com.youthtalk.mapper.toData
import com.youthtalk.model.Category
import com.youthtalk.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val dataSource: DataStoreDataSource,
) : UserRepository {

    override fun getUser(): Flow<User> = flow {
        runCatching { userService.getUser() }
            .onSuccess { response ->
                Log.d("YOON-CHAN", "getUser Success ${response.data}")
                response.data?.let { userResponse ->
                    emit(userResponse.toData())
                }
            }
            .onFailure {
                Log.d("YOON-CHAN", "getUser Error ${it.message}")
                throw it
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
