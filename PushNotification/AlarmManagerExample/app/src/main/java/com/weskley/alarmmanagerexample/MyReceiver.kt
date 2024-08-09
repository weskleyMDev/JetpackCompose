package com.weskley.alarmmanagerexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri

class MyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            showNotification(context, "ALARME", "DISPARADO")
        } catch (e: Exception) {
            Log.d("ERROR:","${e.printStackTrace()}")
        }
    }
}

private fun showNotification(context: Context?, title: String, message: String) {
    val manager = context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "channel_id"
    val channelName = "channel_name"
    val channelDescription = "channel_description"

    val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
    channel.description = channelDescription
    manager.createNotificationChannel(channel)

    val clickIntent = Intent(
        Intent.ACTION_VIEW,
        MY_URI.toUri(),
        context,
        MainActivity::class.java
    ).let { intent ->
        TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentIntent(clickIntent)
        .setAutoCancel(true)

    manager.notify(0, builder.build())
}