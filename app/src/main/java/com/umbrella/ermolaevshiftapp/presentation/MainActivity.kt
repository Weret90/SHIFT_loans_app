package com.umbrella.ermolaevshiftapp.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umbrella.ermolaevshiftapp.R
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_LANGUAGE = "language"
        private const val DEFAULT_LANGUAGE = "ru"

        const val CHANNEL_ID = "channelID"
        private const val NOTIFICATION_CHANNEL_NAME = "Loans App Notification Channel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val language = getPreferences(MODE_PRIVATE)
            .getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE

        val config = resources.configuration
        val locale = Locale(language)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}