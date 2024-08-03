package com.weskley.localnotifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class NotificationService(
    private val context: Context
) {

    private val notificationManager = context.getSystemService(Context
        .NOTIFICATION_SERVICE) as NotificationManager

    fun showSimpleNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_notification_important_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}