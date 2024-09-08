package com.core.navigation.navigator

import android.app.Activity
import android.content.Intent

interface Navigator {
    fun navigateFrom(activity: Activity, intentBuilder: Intent.() -> Intent = { this }, withFinish: Boolean)
}
