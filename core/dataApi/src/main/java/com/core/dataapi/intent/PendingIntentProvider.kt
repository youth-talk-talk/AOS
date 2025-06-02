package com.core.dataapi.intent

import android.app.PendingIntent

interface PendingIntentProvider {
    fun createMainActivityIntent(data: Map<String, Long>): PendingIntent
}
