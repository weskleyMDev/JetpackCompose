package com.weskley.localnotifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

const val CHANNEL_ID = "ch_1"
const val CHANNEL_NAME = "My Channel"
const val NOTIFICATION_ID = 100

class NotificationApplication: Application() {

    private val importance = NotificationManager.IMPORTANCE_HIGH

    override fun onCreate() {
        super.onCreate()

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            importance
        )

        notificationChannel.description = "notification channel from app"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as
                NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}