package com.weskley.hdc_app.service.imp

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.weskley.hdc_app.MainActivity
import com.weskley.hdc_app.R
import com.weskley.hdc_app.constant.Constants
import com.weskley.hdc_app.controller.ARG
import com.weskley.hdc_app.controller.MY_URI
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.receiver.NotificationReceiver
import com.weskley.hdc_app.service.NotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationServiceImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager,
    private val notificationManager: NotificationManager
) : NotificationService {

    override fun createNotification(
        id: Int, title: String, body: String, imageUri: String
    ) {
        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else 0

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

        val soundUri =
            Uri.parse("android.resource://com.weskley.hdc_app/" + R.raw.new_iphone)

        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_circ_branco)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(Constants.HIGH_PRIORITY)
            .setLargeIcon(selectImage(context, imageUri))
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(selectImage(context, imageUri))
                    .bigLargeIcon(null as Bitmap?)
            )
            .setContentIntent(clickIntent)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .setSound(soundUri)
            .addAction(0, "CONFIRMAR", null)
            .addAction(0, "FEEDBACK", clickIntent)
            .setGroup(Constants.GROUP_KEY)
            .setAutoCancel(true)

        val summaryNotification =
            NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_circ_branco)
                .setContentTitle(Constants.SUMMARY_TITLE)
                .setContentText(Constants.SUMMARY_TEXT)
                .setPriority(Constants.HIGH_PRIORITY)
                .setStyle(
                    NotificationCompat
                        .InboxStyle()
                        .setBigContentTitle(Constants.SUMMARY_GROUP_TITLE)
                        .setSummaryText(Constants.SUMMARY_GROUP_TEXT)
                )
                .setGroup(Constants.GROUP_KEY)
                .setAutoCancel(true)
                .setGroupSummary(true)

        notificationManager.apply {
            notify(id, notification.build())
            notify(Constants.REQUEST_CODE, summaryNotification.build())
        }
    }

    override fun setRepeatingAlarm(
        item: CustomNotification
    ) {
        val now = Calendar.getInstance()
        val time = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, item.time.split(":")[0].toInt())
            set(Calendar.MINUTE, item.time.split(":")[1].toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (time.timeInMillis <= now.timeInMillis) {
            time.add(Calendar.DAY_OF_YEAR, 1)
        }
        Log.d("AlarmDebug", "Alarm set for: ${time.timeInMillis}")

        val alarmIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", item.title)
            putExtra("text", item.body)
            putExtra("imgUri", item.image)
            putExtra("id", item.id+1)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id+1,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
            pendingIntent
        )
        Log.d("AlarmDebug", "Alarm with ID ${item.id} created for ${time.time}")
    }

    override fun setDailyAlarm(
        item: CustomNotification
    ) {
        val now = Calendar.getInstance()
        val time = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, item.time.split(":")[0].toInt())
            set(Calendar.MINUTE, item.time.split(":")[1].toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (time.timeInMillis <= now.timeInMillis) {
            time.add(Calendar.DAY_OF_YEAR, 1)
        }
        Log.d("AlarmDebug", "Alarm set for: ${time.timeInMillis}")

        val alarmIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", item.title)
            putExtra("text", item.body)
            putExtra("imgUri", item.image)
            putExtra("id", item.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
//            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.d("AlarmDebug", "Alarm with ID ${item.id} created for ${time.time}")
    }

    override fun resetAlarm(
        item: CustomNotification
    ) {
        val time = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, item.time.split(":")[0].toInt())
            set(Calendar.MINUTE, item.time.split(":")[1].toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        Log.d("AlarmDebug", "Alarm reset for: ${time.timeInMillis}")

        val alarmIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", item.title)
            putExtra("text", item.body)
            putExtra("img", item.image)
            putExtra("hour", item.time.split(":")[0].toInt())
            putExtra("minute", item.time.split(":")[1].toInt())
            putExtra("id", item.id)
        }.let { intent ->
            PendingIntent.getBroadcast(
                context,
                item.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
            alarmIntent
        )
        Log.d("AlarmDebug", "Alarm with ID $item.id created for ${time.time}")
    }

    override fun isAlarmActive(id: Int): Boolean {
        val pendingIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("id", id)
        }.let { intent ->
            PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
        }
        return pendingIntent != null
    }

    override fun cancelRepeatingAlarm(id: Int) {
        if (isAlarmActive(id)) {
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id+1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            Log.d("AlarmDebug", "Alarm with ID $id canceled")
        } else {
            Log.d("AlarmDebug", "No alarm to cancel with ID $id")
        }
    }
    override fun cancelDailyAlarm(id: Int) {
        if (isAlarmActive(id)) {
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            Log.d("AlarmDebug", "Alarm with ID $id canceled")
        } else {
            Log.d("AlarmDebug", "No alarm to cancel with ID $id")
        }
    }
}

private fun selectImage(context: Context, image: String): Bitmap {
    val bitmap: Bitmap? =
        try {
            val uri = Uri.parse(image)
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("NotificationService", "Error selecting image: ${e.message}")
            null
        }
    return bitmap ?: BitmapFactory.decodeResource(context.resources, R.drawable.logo_circ_branco)
}