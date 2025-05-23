package com.youthtalk.repository

import android.content.Context
import android.content.Intent
import com.core.dataapi.repository.SseRepository
import com.youthtalk.sse.SseService
import javax.inject.Inject
import timber.log.Timber

class SseRepositoryImpl @Inject constructor(
    private val context: Context
) : SseRepository {
    override fun startSseService() {
        Timber.e("SseRepositoryImpl start")
        val intent = Intent(context, SseService::class.java)
        context.startService(intent)
    }
}
