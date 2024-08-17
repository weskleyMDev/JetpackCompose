package com.weskley.hdc_app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.weskley.hdc_app.constant.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HdcApp: Application() {
    override fun onCreate() {
        super.onCreate()

        val manager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME,
                Constants.HIGH_IMPORTANCE
            )
            channel.description = Constants.CHANNEL_DESCRIPTION
            manager.createNotificationChannel(channel)
        }
    }
}