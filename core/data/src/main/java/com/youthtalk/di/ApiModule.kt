package com.youthtalk.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.youth.app.core.data.BuildConfig
import com.youthtalk.data.LoginService
import com.youthtalk.model.CommonResponse
import com.youthtalk.model.TokenResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "youths")

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val interceptor =
            Interceptor { chain ->

                val request =
                    chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()

                val response = chain.proceed(request)

                val commonResponse = Gson().fromJson(response.body?.string(), CommonResponse::class.java)

                val token = response.headers["Authorization"]
                val refreshToken = response.headers["Authorization-refresh"]
                if (token != null && refreshToken != null) {
                    val tokenResponse = TokenResponse(token, refreshToken)

                    val newResponseBody = CommonResponse(commonResponse.status, commonResponse.message, commonResponse.code, tokenResponse)
                    response.newBuilder()
                        .code(response.code)
                        .body(newResponseBody.toResponseBody())
                        .build()
                } else {
                    response.newBuilder()
                        .code(response.code)
                        .body(commonResponse.toResponseBody())
                        .build()
                }
            }

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_KEY)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService = retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore
}
