package com.tradingstrategylab.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class ResearchScheduler {
    companion object {
        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<ReadinessWorker>(24, TimeUnit.HOURS)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "daily_research_readiness", ExistingPeriodicWorkPolicy.UPDATE, request
            )
        }
    }
}
