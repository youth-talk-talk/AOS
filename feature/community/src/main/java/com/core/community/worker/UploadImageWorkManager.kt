package com.core.community.worker

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.core.community.utils.FileConverter
import com.core.domain.usercase.post.PostUploadImageUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

@HiltWorker
class UploadImageWorkManager @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted private val params: WorkerParameters,
    private val uploadImageUseCase: PostUploadImageUseCase,
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        var result = Result.failure()
        params.inputData.getString("uri")?.let {
            val uri = it.toUri()
            val file = FileConverter.uriToFile(ctx, uri)
            Log.d("YOON-CHAN", "doWork file length ${file?.length()}")
            file?.let {
                uploadImageUseCase(file)
                    .catch { error ->
                        Log.d("YOON-CHAN", "uploadImageUseCase failure ${error.message}")
                        result = Result.failure()
                    }
                    .collectLatest { uri ->
                        Log.d("YOON-CHAN", "uploadImageUseCase success $uri")
                        val data = Data.Builder()
                            .putString("uri", uri)
                            .build()
                        result = Result.success(data)
                    }
            }
        } ?: Result.failure()
        return result
    }
}
