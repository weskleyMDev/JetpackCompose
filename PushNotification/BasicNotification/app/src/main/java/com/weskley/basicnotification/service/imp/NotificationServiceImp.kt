package com.weskley.basicnotification.service.imp

import android.content.Context
import androidx.core.app.NotificationCompat
import com.weskley.basicnotification.module.CHANNEL_ID
import com.weskley.basicnotification.R
import com.weskley.basicnotification.service.NotificationService

class NotificationServiceImp(
    private val context: Context
): NotificationService {
    override fun createNotification(title: String, message: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }
}