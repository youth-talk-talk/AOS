package com.core.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.ACCESS_TOKEN
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.CATEGORIES
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.REFRESH_TOKEN
import com.core.datastore.datasource.DataStoreDataSource.PreferencesKey.REVIEW_CATEGORIES
import com.youthtalk.model.Category
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
}
