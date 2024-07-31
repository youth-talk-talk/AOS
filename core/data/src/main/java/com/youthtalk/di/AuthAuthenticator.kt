package com.youthtalk.di

import android.util.Log
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
        const val AUTHORIZATION_REFRESH = "Authorization-refresh"
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            dataStoreDataSource.getRefreshToken().first()
        }
        val requestAuthorizationHeader = response.request.headers[AUTHORIZATION]
        val requestAuthorizationRefreshHeader = response.request.headers[AUTHORIZATION_REFRESH]
        Log.d(
            "YOON-CHAN",
            "AuthAuthenticator refreshToken $refreshToken," +
                "\n requestAuthorizationHeader $requestAuthorizationHeader," +
                "\n requestAuthorizationRefreshHeader $requestAuthorizationRefreshHeader," +
                "\n message ${response.body?.string()}",
        )
        if (refreshToken == null || requestAuthorizationRefreshHeader == "Bearer $refreshToken") {
            Log.d("YOON-CHAN", "no refresh Token")
            response.close()
            return null
        }

        Log.d("YOON-CHAN", "Change Header")
        return response.request.newBuilder()
            .addHeader(AUTHORIZATION_REFRESH, "Bearer $refreshToken")
            .build()
    }
}
