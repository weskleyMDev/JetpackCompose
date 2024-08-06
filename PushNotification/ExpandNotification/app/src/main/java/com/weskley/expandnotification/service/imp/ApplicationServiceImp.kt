package com.weskley.expandnotification.service.imp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat

class ApplicationServiceImp(private val context: Context): ApplicationService {
    override fun createChannel(
        id: String,
        name: String,
        description: String,
        importance: Int
    ): NotificationManager {
        val channel = NotificationChannel(
            id,
            name,
            importance
        )
        channel.description = description
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return notificationManager
    }

    override fun createNotification(
        channelId: String,
        icon: Int,
        title: String,
        message: String,
        priority: Int,
        image: Bitmap?,
        style: NotificationCompat.Style?,
        cancel: Boolean
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(priority)
            .setLargeIcon(image)
            .setStyle(
                style ?: NotificationCompat
                        .BigPictureStyle()
                        .bigPicture(image)
                        .bigLargeIcon(null as Bitmap?)
            )
            .setAutoCancel(cancel)
    }
}