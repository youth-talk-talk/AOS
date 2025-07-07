package com.youthtalk.sse

import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import timber.log.Timber

class SseClient @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    private var es: EventSource? = null

    fun start(token: String, url: String, onMessage: (String) -> Unit) {
        runBlocking {
            es?.cancel()
            es = null
            delay(500L)
        }

        Timber.e("SseClient start")
        val request = Request.Builder().url(url).addHeader("Authorization", token).build()
        EventSources.createFactory(okHttpClient)
            .newEventSource(
                request,
                object : EventSourceListener() {
                    override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                        Timber.e("알림 시작 메세지 $data")
                        onMessage(data)
                    }

                    override fun onClosed(eventSource: EventSource) {
                        Timber.e("newEventSource onClosed")
                        super.onClosed(eventSource)
                    }

                    override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                        Timber.e("newEventSource onFailure error $t")
                        super.onFailure(eventSource, t, response)
                    }

                    override fun onOpen(eventSource: EventSource, response: Response) {
                        Timber.e("newEventSource onOpen ${response.body}")
                        super.onOpen(eventSource, response)
                    }
                }
            )
            .also {
                es = it
            }
        Timber.e("eventSource start last lane $es")
    }

    fun stop() {
        Timber.e("SseClient stop eventSource is null? $es")
        es?.cancel()
        es = null
    }
}
