package com.core.community.worker

import android.content.Context
import android.net.Uri
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

object UploadWork {
    fun uploadWorkManager(context: Context, scope: CoroutineScope, uuid: UUID, uri: Uri, changeUri: (String) -> Unit) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString("uri", uri.toString())
            .build()

        val workRequest = OneTimeWorkRequestBuilder<UploadImageWorkManager>()
            .setId(uuid)
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork(
            uri.toString(),
            ExistingWorkPolicy.KEEP,
            workRequest,
        )

        scope.launch {
            workManager.getWorkInfoByIdFlow(uuid)
                .catch {
                    Timber.e("workManager.getWorkInfoByIdFlow error " + it.message)
                }
                .collect { workInfo ->
                    if (workInfo != null) {
                        if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                            val newUri = workInfo.outputData.getString("uri") ?: uri.toString()
                            changeUri(newUri)
                        }
                    }
                }
        }
    }
}
