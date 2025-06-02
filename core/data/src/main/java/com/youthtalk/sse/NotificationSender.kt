package com.youthtalk.sse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.youthtalk.model.notification.SseNotification

object NotificationSender {
    private const val CHANNEL_ID = "youth_talk_sse_message"

    fun notify(context: Context, data: SseNotification, pendingIntent: PendingIntent) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "SSE 알림", NotificationManager.IMPORTANCE_DEFAULT)
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(data.title)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(data.message)
            )
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
