package com.youthtalk.repository

import android.content.Context
import com.core.dataapi.intent.PendingIntentProvider
import com.core.dataapi.repository.SseRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.model.notification.SseNotification
import com.youthtalk.sse.NotificationSender
import com.youthtalk.sse.SseClient
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber

class SseRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dataSource: DataStoreDataSource,
    private val sseClient: SseClient,
    private val mainPendingIntentProvider: PendingIntentProvider
) : SseRepository {
    override fun startSseService() {
        val token: String = runBlocking {
            dataSource.getAccessToken().first()
        } ?: return

        Timber.e("startSseService token $token")
        sseClient.start("Bearer $token", "http://13.209.36.122/api/v1/notifications/subscribe") { message ->
            Timber.e("SseService sseClient message $message")
            try {
                val notification = Json.decodeFromString<SseNotification>(message)
                val data = mutableMapOf<String, Long>()
                notification.postId?.let {
                    data.put("postId", it)
                }
                notification.policyId?.let {
                    data.put("policyId", it)
                }
                val intent = mainPendingIntentProvider.createMainActivityIntent(data)
                NotificationSender.notify(context, notification, intent)
            } catch (e: SerializationException) {
                Timber.e("SseService SerializationException $e")
            } catch (e: IllegalArgumentException) {
                Timber.e("SseService IllegalArgumentException $e")
            }
        }
    }

    override fun stopSseService() {
        sseClient.stop()
    }
}
