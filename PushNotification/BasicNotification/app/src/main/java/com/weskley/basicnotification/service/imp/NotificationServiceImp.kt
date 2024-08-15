package com.weskley.basicnotification.service.imp

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.weskley.basicnotification.MainActivity
import com.weskley.basicnotification.R
import com.weskley.basicnotification.module.CHANNEL_ID
import com.weskley.basicnotification.navigation.LNAME
import com.weskley.basicnotification.navigation.NAME
import com.weskley.basicnotification.navigation.URI
import com.weskley.basicnotification.service.NotificationService

class NotificationServiceImp(
    private val context: Context
): NotificationService {
    override fun createNotification(context: Context, title: String, message: String): NotificationCompat.Builder {
        val pendingIntent = Intent(
            Intent.ACTION_VIEW,
            "$URI/$NAME=NOTIFICATION&$LNAME=HILT".toUri(),
            context,
            MainActivity::class.java
        ).let {
            PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }
}