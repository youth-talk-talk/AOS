package com.core.datastore.datasource

import com.youthtalk.model.Category
import com.youthtalk.model.EmploymentCode
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun hasToken(): Flow<Boolean>
    fun getCategoryFilter(): Flow<List<Category>>
    fun getEmployCode(): Flow<List<EmploymentCode>?>
    fun getReviewCategoryFilter(): Flow<List<Category>>
    fun getAge(): Flow<Int?>
    fun getFinish(): Flow<Boolean>
    suspend fun setCategoryFilter(categories: List<Category>)
    suspend fun setEmployCodeFilter(employmentCodes: List<EmploymentCode>)
    suspend fun setReviewCategoryFilter(categories: List<Category>)
    suspend fun setAge(age: Int)
    suspend fun setFinish(isFinish: Boolean)
}
