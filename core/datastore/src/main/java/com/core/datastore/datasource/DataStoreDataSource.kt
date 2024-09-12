package com.core.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.ACCESS_TOKEN
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.AGE
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.CATEGORIES
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.EMPLOY_CODE
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.IS_FINISH
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.RECENT_LIST
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.REFRESH_TOKEN
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.REVIEW_CATEGORIES
import com.youthtalk.model.Category
import com.youthtalk.model.EmploymentCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import javax.inject.Inject

class DataStoreDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DataSource {
    object PreferencesKey {
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        val CATEGORIES = stringPreferencesKey("CATEGORIES")
        val REVIEW_CATEGORIES = stringPreferencesKey("REVIEW_CATEGORIES")
        val AGE = intPreferencesKey("AGE")
        val EMPLOY_CODE = stringPreferencesKey("EMPLOY_CODE")
        val RECENT_LIST = stringPreferencesKey("RECENT_LIST")
        val IS_FINISH = booleanPreferencesKey("IS_FINISH")
    }

    override fun hasToken(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN] != null && preferences[REFRESH_TOKEN] != null
    }

    fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN]
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN]
        }
    }

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN] = token
        }
    }

    override fun getCategoryFilter(): Flow<List<Category>> = dataStore.data.map { prefs ->
        prefs[CATEGORIES]?.let { categories ->
            Json.decodeFromString<List<Category>>(categories)
        } ?: Category.entries
    }

    override fun getReviewCategoryFilter(): Flow<List<Category>> = dataStore.data.map { prefs ->
        prefs[REVIEW_CATEGORIES]?.let { categories ->
            Json.decodeFromString<List<Category>>(categories)
        } ?: Category.entries
    }

    override fun getRecentSearchList(): Flow<List<String>> = dataStore.data.map { prefs ->
        prefs[RECENT_LIST]?.split(",") ?: listOf()
    }

    override suspend fun setCategoryFilter(categories: List<Category>) {
        val listToString = Json.encodeToJsonElement<List<Category>>(categories)
        dataStore.edit { prefs ->
            prefs[CATEGORIES] = listToString.toString()
        }
    }

    override suspend fun setReviewCategoryFilter(categories: List<Category>) {
        val listToString = Json.encodeToJsonElement<List<Category>>(categories)
        dataStore.edit { prefs ->
            prefs[REVIEW_CATEGORIES] = listToString.toString()
        }
    }

    override fun getEmployCode(): Flow<List<EmploymentCode>?> = dataStore.data.map { prefs ->
        prefs[EMPLOY_CODE]?.let { employCode ->
            Json.decodeFromString<List<EmploymentCode>>(employCode)
        }
    }

    override suspend fun setEmployCodeFilter(employmentCodes: List<EmploymentCode>?) {
        val listToString = Json.encodeToJsonElement<List<EmploymentCode>>(employmentCodes ?: listOf())
        dataStore.edit { prefs ->
            prefs[EMPLOY_CODE] = listToString.toString()
        }
    }

    override fun getAge(): Flow<Int?> = dataStore.data.map { prefs ->
        prefs[AGE]
    }

    override suspend fun setAge(age: Int?) {
        dataStore.edit { prefs ->
            age?.let { prefs[AGE] = age } ?: prefs.remove(AGE)
        }
    }

    override fun getFinish(): Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[IS_FINISH] ?: false
    }

    override suspend fun setFinish(isFinish: Boolean?) {
        dataStore.edit { prefs ->
            prefs[IS_FINISH] = isFinish ?: false
        }
    }

    override suspend fun setRecentList(list: List<String>) {
        dataStore.edit { prefs ->
            prefs[RECENT_LIST] = list.joinToString(",")
        }
    }

    override suspend fun clearData() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
