package com.youthtalk.di

import com.core.datastore.datasource.DataStoreDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val dataStoreDataSource: DataStoreDataSource,
) : Authenticator {

    companion object {
        const val AUTHORIZATION = "Authorization"
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            dataStoreDataSource.getRefreshToken().first()
        }

        if (refreshToken == null) {
            response.close()
            return null
        }

        return response.request.newBuilder()
            .header(AUTHORIZATION, "Bearer $refreshToken")
            .build()
    }
}
