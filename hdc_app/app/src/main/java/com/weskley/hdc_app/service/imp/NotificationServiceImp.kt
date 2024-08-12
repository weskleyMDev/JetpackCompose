package com.weskley.hdc_app.service.imp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.weskley.hdc_app.service.NotificationService

class NotificationServiceImp(
    private val context: Context
): NotificationService {

    override fun createChannel(
        id: String,
        name: String,
        description: String,
        importance: Int
    ): NotificationManager {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            id,
            name,
            importance
        )
        channel.description = description
        notificationManager.createNotificationChannel(channel)

        return notificationManager
    }

    override fun createNotification(
        channelId: String,
        icon: Int,
        title: String,
        text: String,
        priority: Int,
        image: Bitmap?,
        style: NotificationCompat.Style?,
        cancel: Boolean
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(priority)
            .setLargeIcon(image)
            .setStyle(style)
            .setAutoCancel(cancel)
    }
}