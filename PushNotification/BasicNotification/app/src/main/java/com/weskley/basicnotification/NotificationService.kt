package com.weskley.basicnotification

import androidx.core.app.NotificationCompat

interface NotificationService {
    fun createNotification(title: String, message: String):
            NotificationCompat.Builder
}