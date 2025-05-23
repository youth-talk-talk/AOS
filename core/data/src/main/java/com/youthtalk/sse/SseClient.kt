package com.youthtalk.sse

import com.youthtalk.di.ApiModule
import javax.inject.Inject
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import timber.log.Timber

class SseClient @Inject constructor(
    @ApiModule.Sse private val okHttpClient: OkHttpClient
) {
    private var eventSource: EventSource? = null

    fun start(url: String, onMessage: (String) -> Unit) {
        Timber.e("SseClient start")
        val request = Request.Builder().url(url).build()
        EventSources.createFactory(okHttpClient)
            .newEventSource(
                request,
                object : EventSourceListener() {
                    override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                        Timber.e("알림 시작 메세지 $data")
                        onMessage(data)
                    }
                }
            ).also {
                eventSource = it
            }
    }

    fun stop() {
        Timber.e("SseClient stop")
        eventSource?.cancel()
    }
}
