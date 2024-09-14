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
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.gson.Gson
import com.weskley.hdc_app.MainActivity
import com.weskley.hdc_app.R
import com.weskley.hdc_app.constant.Constants
import com.weskley.hdc_app.controller.MEDICINE
import com.weskley.hdc_app.controller.MEDICINE_AMOUNT
import com.weskley.hdc_app.controller.MEDICINE_ID
import com.weskley.hdc_app.controller.MY_URI
import com.weskley.hdc_app.controller.TIME
import com.weskley.hdc_app.model.Medicine
import com.weskley.hdc_app.receiver.NotificationReceiver
import com.weskley.hdc_app.service.NotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationServiceImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager,
    private val notificationManager: NotificationManager
) : NotificationService {

    override fun createNotification(
        id: Int, name: String, amount: String, type: String, imagePath: String, time: String
    ) {
        val flag =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val notificationIntent = Intent(
            Intent.ACTION_VIEW,
            "$MY_URI/$MEDICINE=$name&$TIME=$type&$MEDICINE_ID=$id&$MEDICINE_AMOUNT=$amount".toUri(),
            context,
            MainActivity::class.java
        ).let { intent ->
            TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(id + 1, flag)
            }
        }

        val largeIcon = selectImage(context, imagePath)
        val soundUri =
            Uri.parse("android.resource://com.weskley.hdc_app/" + R.raw.new_iphone)

        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_circ_branco)
            .setContentTitle(name)
            .setContentText("$amount - $type")
            .setPriority(Constants.HIGH_PRIORITY)
            .setLargeIcon(largeIcon)
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(largeIcon)
                    .bigLargeIcon(null as Bitmap?)
            )
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .setSound(soundUri)
            .setContentIntent(notificationIntent)
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
                .setOngoing(true)
                .setAutoCancel(true)
                .setGroupSummary(true)

        notificationManager.apply {
            notify(id, notification.build())
            notify(Constants.REQUEST_CODE, summaryNotification.build())
        }
    }

    override fun setDailyAlarm(medicine: Medicine) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, medicine.time.split(":")[0].toInt())
            set(Calendar.MINUTE, medicine.time.split(":")[1].toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        val triggerTime = calendar.timeInMillis
        val gson = Gson()
        val medicineJson = gson.toJson(medicine)
        val pendingIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("medicineJson", medicineJson)
        }.let { intent ->
            PendingIntent.getBroadcast(
                context,
                medicine.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        Log.d(
            "AlarmDebug",
            "Alarm with ID ${medicine.id} created for ${calendar.time}"
        )
        Toast.makeText(context, "Alarme [${medicine.name}] ativado para [${medicine.time}]", Toast.LENGTH_SHORT).show()
    }

    override fun setRepeatingAlarm(medicine: Medicine) {
        val formatter = SimpleDateFormat("HH:mm - dd/MM", Locale.getDefault())
        val now = Calendar.getInstance()
        val initialHour = now.get(Calendar.HOUR_OF_DAY)
        val initialMinute = now.get(Calendar.MINUTE)
        val repetitionHours = medicine.repetition.toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, initialHour)
            set(Calendar.MINUTE, initialMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        calendar.add(Calendar.HOUR_OF_DAY, repetitionHours)
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        val triggerTime = calendar.timeInMillis

        val gson = Gson()
        val medicineJson = gson.toJson(medicine)
        val pendingIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("medicineJson", medicineJson)
        }.let { intent ->
            PendingIntent.getBroadcast(
                context,
                medicine.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        Log.d(
            "AlarmDebug",
            "Alarm with ID ${medicine.id} created for ${calendar.time} to repeat each ${medicine.repetition} hours"
        )
        Toast.makeText(context, "Próximo alarme às ${formatter.format(calendar.time)}", Toast.LENGTH_SHORT).show()
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

    override fun cancelAlarm(id: Int) {
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

private fun selectImage(context: Context, imagePath: String): Bitmap {
    Log.d("NotificationService", "Selecting image from path: $imagePath")
    return try {
        val uri = if (imagePath.startsWith("/")) {
            Uri.fromFile(File(imagePath))
        } else {
            Uri.parse(imagePath)
        }

        val inputStream = when (uri.scheme) {
            "content" -> {
                Log.d("NotificationService", "Opening input stream from content URI")
                context.contentResolver.openInputStream(uri)
            }
            "file" -> {
                Log.d("NotificationService", "Opening input stream from file URI")
                FileInputStream(File(uri.path!!))
            }
            else -> {
                Log.e("NotificationService", "Unsupported URI scheme: ${uri.scheme}")
                null
            }
        }

        inputStream?.use { input ->
            BitmapFactory.decodeStream(input)
                ?: BitmapFactory.decodeResource(context.resources, R.drawable.logo_circ_branco)
        } ?: BitmapFactory.decodeResource(context.resources, R.drawable.logo_circ_branco)
    } catch (e: Exception) {
        Log.e("NotificationService", "Error selecting image: ${e.message}", e)
        BitmapFactory.decodeResource(context.resources, R.drawable.logo_circ_branco)
    }
}