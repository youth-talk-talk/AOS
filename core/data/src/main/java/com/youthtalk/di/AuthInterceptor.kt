package com.youthtalk.di

import android.util.Log
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.model.CommonResponse
import com.youthtalk.model.toResponseBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection.HTTP_OK
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStoreDataSource: DataStoreDataSource,
) : Interceptor {

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val AUTHORIZATION_REFRESH = "Authorization-refresh"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String = runBlocking {
            dataStoreDataSource.getAccessToken().first()
        } ?: return errorResponse(chain.request())

        val request = chain.request().newBuilder().header(AUTHORIZATION, "Bearer $token").build()

        val response = chain.proceed(request)

        Log.d("YOON-CHAN", "AuthInterceptor response code ${response.code}")
        if (response.code == HTTP_OK) {
            val newAccessToken: String = response.header(AUTHORIZATION, null) ?: return response
            val newRefreshToken: String = response.header(AUTHORIZATION_REFRESH, null) ?: return response

            Log.d("YOON-CHAN", "new Access Token = $newAccessToken, new Refresh Token $newRefreshToken")
            CoroutineScope(Dispatchers.IO).launch {
                val currentAccessToken = dataStoreDataSource.getAccessToken().first()
                if (currentAccessToken != newAccessToken) {
                    dataStoreDataSource.saveAccessToken(newAccessToken)
                    dataStoreDataSource.saveRefreshToken(newRefreshToken)
                    Log.d("YOON-CHAN", "save new Access Token")
                }
            }
        }

        return response
    }

    private fun errorResponse(request: Request): Response {
        val responseBody = CommonResponse<Unit>(
            code = "M01",
            status = HTTP_UNAUTHORIZED,
            message = "",
            data = null,
        )

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(HTTP_UNAUTHORIZED)
            .message("")
            .body(toResponseBody(responseBody))
            .build()
    }
}
