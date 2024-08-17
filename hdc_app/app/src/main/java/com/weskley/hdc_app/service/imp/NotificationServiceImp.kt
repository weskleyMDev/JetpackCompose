package com.weskley.hdc_app.service.imp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.weskley.hdc_app.MainActivity
import com.weskley.hdc_app.controller.ARG
import com.weskley.hdc_app.controller.MY_URI
import com.weskley.hdc_app.receiver.NotificationReceiver
import com.weskley.hdc_app.service.NotificationService
import java.util.Calendar

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                id,
                name,
                importance
            )
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }

    override fun createNotification(
        channelId: String,
        arg: String,
        icon: Int,
        title: String,
        text: String,
        priority: Int,
        image: Bitmap?,
        style: NotificationCompat.Style?,
        cancel: Boolean,
    ): NotificationCompat.Builder {
        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            "$MY_URI/$ARG=$arg".toUri(),
            context,
            MainActivity::class.java
        ).let { intent ->
            TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }
        }
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(priority)
            .setLargeIcon(image)
            .setStyle(style)
            .setContentIntent(clickIntent)
            .setAutoCancel(cancel)
    }

    override fun scheduleNotification(
        context: Context,
        hour: Int,
        minute: Int,
        title: String?,
        text: String?,
        img: Int?
    ) {
        val time = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("text", text)
            putExtra("img", img)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.timeInMillis, pendingIntent)
    }
}