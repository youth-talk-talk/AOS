package com.youthtalk.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.core.dataapi.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreRepository {
    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }

    override fun hasToken(): Flow<Boolean> = flow {
        val accessToken = stringPreferencesKey(ACCESS_TOKEN)
        val refreshToken = stringPreferencesKey(REFRESH_TOKEN)
        dataStore.data.map { preferences ->
            preferences[accessToken] != null && preferences[refreshToken] != null
        }.collect { emit(it) }
    }
}
