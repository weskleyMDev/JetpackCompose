package com.weskley.hdc_app.service

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat

interface NotificationService {
    fun createChannel(
        id: String,
        name: String,
        description: String,
        importance: Int
    ): NotificationManager

    fun createNotification(
        channelId: String,
        icon: Int,
        title: String,
        text: String,
        priority: Int,
        image: Bitmap? = null,
        style: NotificationCompat.Style? = null,
        cancel: Boolean = true,
        arg: String
    ): NotificationCompat.Builder

    fun scheduleNotification(
        context: Context, hour: Int, minute: Int,
        title: String?,
        text: String?
    )
}