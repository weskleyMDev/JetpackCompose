package com.weskley.hdc_app.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.weskley.hdc_app.MainActivity
import com.weskley.hdc_app.R
import com.weskley.hdc_app.controller.MY_URI
import com.weskley.hdc_app.service.imp.NotificationServiceImp
import com.weskley.hdc_app.viewmodel.AlarmViewModel

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val title = intent.getStringExtra("title") ?: ""
            val text = intent.getStringExtra("text") ?: ""
            val service = AlarmViewModel(NotificationServiceImp(context))
            service.showNotification(context, title, text)
        } catch (e: Exception) {
            Log.d("ERROR:","${e.printStackTrace()}")
        }
    }
}

fun showNotification(context: Context?, title: String, message: String) {
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