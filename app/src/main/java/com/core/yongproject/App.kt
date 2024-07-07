package com.core.yongproject

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.youth.app.com.youth.yongproject.app.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
internal class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}
