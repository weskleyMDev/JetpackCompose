package com.weskley.hdc_app.service.imp

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.weskley.hdc_app.MainActivity
import com.weskley.hdc_app.R
import com.weskley.hdc_app.constant.Constants
import com.weskley.hdc_app.controller.ARG
import com.weskley.hdc_app.controller.MY_URI
import com.weskley.hdc_app.receiver.NotificationReceiver
import com.weskley.hdc_app.service.NotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class NotificationServiceImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager,
    private val notificationManager: NotificationManager
): NotificationService {

    override fun createNotification(
        title: String, body: String, image: Int
    ) {
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

        val soundUri = Uri.parse("android.resource://com.weskley.hdc_app/" + R.raw.new_iphone)

        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_circ_branco)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(Constants.HIGH_PRIORITY)
            .setLargeIcon(selectImage(context.resources, image))
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(selectImage(context.resources, image))
                    .bigLargeIcon(null as Bitmap?)
            )
            .setContentIntent(clickIntent)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .setSound(soundUri)
            .addAction(0, "CONFIRMAR", null)
            .addAction(0, "FEEDBACK", clickIntent)
            .setGroup(Constants.GROUP_KEY)
            .setAutoCancel(true)

        val summaryNotification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_circ_branco)
            .setContentTitle(Constants.SUMMARY_TITLE)
            .setContentText(Constants.SUMMARY_TEXT)
            .setPriority(Constants.HIGH_PRIORITY)
            .setStyle(
                NotificationCompat
                    .InboxStyle()
                    .setSummaryText(Constants.SUMMARY_GROUP_TEXT)
                    .setBigContentTitle(Constants.SUMMARY_GROUP_TITLE)
            )
            .setGroup(Constants.GROUP_KEY)
            .setAutoCancel(true)
            .setGroupSummary(true)

        notificationManager.notify(Random.nextInt(), notification.build())
        notificationManager.notify(Constants.REQUEST_CODE, summaryNotification.build())

    }

    override fun setAlarm(id: Int, title: String, body: String, image: Int, hour: Int, minute: Int) {
        val time = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val alarmIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("text", body)
            putExtra("img", image)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.timeInMillis, pendingIntent)
    }

    override fun cancelAlarm(id: Int) {
        val pendingIntent = Intent(context, NotificationReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager.cancel(pendingIntent)
    }

}

private fun selectImage(resource: Resources, image: Int): Bitmap {
    return BitmapFactory.decodeResource(resource, image)
}