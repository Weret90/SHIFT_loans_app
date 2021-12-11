package com.umbrella.ermolaevshiftapp.presentation

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.umbrella.ermolaevshiftapp.R
import java.util.concurrent.TimeUnit

class NotificationWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    companion object {
        private const val NOTIFICATION_ID = 101
        private const val NOTIFICATION_PERIOD_SEC = 20L

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(NOTIFICATION_PERIOD_SEC, TimeUnit.SECONDS)
                .build()
        }
    }

    override fun doWork(): Result {
        try {
            sendNotification()
        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.success()
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_ID, builder.build())
    }
}