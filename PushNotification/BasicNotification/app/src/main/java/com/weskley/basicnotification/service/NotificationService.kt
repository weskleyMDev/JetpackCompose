package com.weskley.basicnotification.service

import androidx.core.app.NotificationCompat

interface NotificationService {
    fun createNotification(title: String, message: String):
            NotificationCompat.Builder
}