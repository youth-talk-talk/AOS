package com.youthtalk.di

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.core.dataapi.intent.PendingIntentProvider
import com.core.navigation.navigator.MainNavigator
import com.youthtalk.MainActivity
import com.youthtalk.extentions.startActivityWithAnimation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

internal class MainNavigatorImpl @Inject constructor() : MainNavigator {
    override fun navigateFrom(activity: Activity, intentBuilder: Intent.() -> Intent, withFinish: Boolean) {
        activity.startActivityWithAnimation<MainActivity>(
            intentBuilder = intentBuilder,
            withFinish = withFinish
        )
    }
}

internal class MainActivityPendingIntent @Inject constructor(
    @ApplicationContext private val context: Context
) : PendingIntentProvider {
    override fun createMainActivityIntent(data: Map<String, Long>): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        data.forEach { (name, value) ->
            intent.putExtra(name, value)
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MainModule {

    @Singleton
    @Binds
    abstract fun bindLoginNavigator(mainNavigatorImpl: MainNavigatorImpl): MainNavigator

    @Singleton
    @Binds
    abstract fun bindMainPendingProvider(mainActivityPendingIntent: MainActivityPendingIntent): PendingIntentProvider
}
