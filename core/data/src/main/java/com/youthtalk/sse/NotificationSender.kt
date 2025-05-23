package com.youthtalk.sse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

object NotificationSender {
    private const val CHANNEL_ID = "youth_talk_sse_message"

    fun notify(context: Context, message: String) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "SSE 알림", NotificationManager.IMPORTANCE_DEFAULT)
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("서버 메시지")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
