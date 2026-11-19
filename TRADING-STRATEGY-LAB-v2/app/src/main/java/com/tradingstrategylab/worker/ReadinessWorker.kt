package com.tradingstrategylab.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReadinessWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Placeholder: a production research pipeline and notifications must be added.
        return Result.success()
    }
}
