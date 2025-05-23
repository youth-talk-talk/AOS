package com.youthtalk.sse

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class SseService : Service() {

    @Inject
    lateinit var sseClient: SseClient

    override fun onCreate() {
        super.onCreate()
        Timber.e("SseService start")
        startForegroundNotification()

        sseClient.start("http://13.209.36.122/api/v1/notifications/subscribe") { message ->
            Timber.e("SseService sseClient message $message")
            NotificationSender.notify(this, message)
        }
    }

    override fun onDestroy() {
        Timber.e("SseService destroy")
        sseClient.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundNotification() {
        val channelId = "foreground_sse"

        val channel = NotificationChannel(channelId, "SSE Foreground", NotificationManager.IMPORTANCE_MIN)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("청년 톡톡 이벤트 메세지 받기")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        Timber.e("SseService startForegroundNotification")
        NotificationSender.notify(this, "startForegroundNotification 실행")
        startForeground(1, notification)
    }
}
