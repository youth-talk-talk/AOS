package com.youthtalk.di

import android.util.Log
import com.core.datastore.datasource.DataStoreDataSource
import com.youth.app.core.data.BuildConfig
import com.youthtalk.data.HomeService
import com.youthtalk.data.LoginService
import com.youthtalk.data.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Login

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Main

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideConverterFactory(json: Json): Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Singleton
    @Provides
    @Login
    fun provideOkHttp(dataStoreDataSource: DataStoreDataSource): OkHttpClient {
        val interceptor =
            Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()

                val response = chain.proceed(request)
                val token = response.headers["Authorization"]
                val refreshToken = response.headers["Authorization-refresh"]
                if (token != null && refreshToken != null) {
                    Log.d("YOON-CHAN", "token $ refreshToken Saved \n $token \n $refreshToken")
                    CoroutineScope(Dispatchers.IO).launch {
                        dataStoreDataSource.saveAccessToken(token)
                        dataStoreDataSource.saveRefreshToken(refreshToken)
                    }
                }

                response
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
    @Main
    fun provideAuthInterceptor(dataStoreDataSource: DataStoreDataSource): AuthInterceptor = AuthInterceptor(dataStoreDataSource)

    @Provides
    @Singleton
    @Main
    fun provideAuthAuthenticator(dataStoreDataSource: DataStoreDataSource): Authenticator = AuthAuthenticator(dataStoreDataSource)

    @Provides
    @Singleton
    @Main
    fun provideMainOkHttpClient(@Main authInterceptor: AuthInterceptor, @Main authenticator: Authenticator): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .authenticator(authenticator)
            .build()
    }

    @Singleton
    @Provides
    @Login
    fun provideLoginRetrofit(@Login okHttpClient: OkHttpClient, converterFactory: Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_KEY)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Main
    fun provideMainRetrofit(@Main okHttpClient: OkHttpClient, converterFactory: Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_KEY)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideHomeService(@Main retrofit: Retrofit): HomeService = retrofit.create(HomeService::class.java)

    @Provides
    @Singleton
    fun provideLoginService(@Login retrofit: Retrofit): LoginService = retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideUserService(@Main retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)
}
