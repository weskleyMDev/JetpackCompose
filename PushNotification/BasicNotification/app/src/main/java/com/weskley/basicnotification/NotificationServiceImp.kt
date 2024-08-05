package com.weskley.basicnotification

import android.content.Context
import androidx.core.app.NotificationCompat

class NotificationServiceImp(
    private val context: Context
): NotificationService {
    override fun createNotification(title: String, message: String): NotificationCompat.Builder {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        return notification
    }
}