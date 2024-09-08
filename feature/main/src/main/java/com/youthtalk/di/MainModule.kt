package com.youthtalk.di

import android.app.Activity
import android.content.Intent
import com.core.navigation.navigator.MainNavigator
import com.youthtalk.MainActivity
import com.youthtalk.extentions.startActivityWithAnimation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

internal class MainNavigatorImpl @Inject constructor() : MainNavigator {
    override fun navigateFrom(activity: Activity, intentBuilder: Intent.() -> Intent, withFinish: Boolean) {
        activity.startActivityWithAnimation<MainActivity>(
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MainModule {

    @Singleton
    @Binds
    abstract fun bindLoginNavigator(mainNavigatorImpl: MainNavigatorImpl): MainNavigator
}
