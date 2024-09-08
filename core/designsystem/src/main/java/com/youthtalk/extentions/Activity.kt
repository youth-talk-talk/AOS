package com.youthtalk.extentions

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity> Activity.startActivityWithAnimation(intentBuilder: Intent.() -> Intent = { this }, withFinish: Boolean = true) {
    startActivity(Intent(this, T::class.java).intentBuilder())
    if (withFinish) finish()
}
