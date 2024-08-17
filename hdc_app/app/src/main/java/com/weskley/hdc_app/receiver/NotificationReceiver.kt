package com.weskley.hdc_app.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.weskley.hdc_app.MainActivity
import com.weskley.hdc_app.R
import com.weskley.hdc_app.constant.Constants
import com.weskley.hdc_app.controller.ARG
import com.weskley.hdc_app.controller.MY_URI

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val title = intent.getStringExtra("title") ?: ""
            val text = intent.getStringExtra("text") ?: ""
            val img = intent.getIntExtra("img", 0)
            showNotification(context, title, text, img)
        } catch (e: Exception) {
            Log.d("ERROR:", "${e.printStackTrace()}")
        }
    }
}

private fun showNotification(context: Context, title: String, text: String, image: Int) {

    val manager = context.getSystemService(NotificationManager::class.java)

    val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) PendingIntent.FLAG_IMMUTABLE else 0

    val clickIntent = Intent(
        Intent.ACTION_VIEW,
        "$MY_URI/$ARG=$title".toUri(),
        context,
        MainActivity::class.java
    ).let { intent ->
        TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(Constants.PENDING_CODE, flag)
        }
    }

    val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
        .setSmallIcon(R.drawable.logo_circ_branco)
        .setContentTitle(title)
        .setContentText(text)
        .setPriority(Constants.HIGH_PRIORITY)
        .setLargeIcon(selectImage(context.resources, image))
        .setStyle(
            NotificationCompat
                .BigPictureStyle()
                .bigPicture(selectImage(context.resources, image))
                .bigLargeIcon(null as Bitmap?)
        )
        .setContentIntent(clickIntent)
        .setVibrate(longArrayOf(0, 500, 1000))
        .setSound(soundUri)
        .setAutoCancel(true)

    manager.notify(Constants.REQUEST_CODE, notification.build())
}

private fun selectImage(resource: Resources, image: Int): Bitmap {
    return BitmapFactory.decodeResource(resource, image)
}