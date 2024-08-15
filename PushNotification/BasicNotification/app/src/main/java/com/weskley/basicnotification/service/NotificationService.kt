package com.weskley.basicnotification.service

import android.content.Context
import androidx.core.app.NotificationCompat

interface NotificationService {
    fun createNotification(context: Context, title: String, message: String):
            NotificationCompat.Builder
}