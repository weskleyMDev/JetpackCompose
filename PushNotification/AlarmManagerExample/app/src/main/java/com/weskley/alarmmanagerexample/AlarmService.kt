package com.weskley.alarmmanagerexample

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.util.Calendar

class AlarmService {

    fun setAlarm(context: Context?, h: Int, m: Int) {
        val time = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, h)
            set(Calendar.MINUTE, m)
        }
        val alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, MyReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.timeInMillis, alarmIntent)
    }

    fun cancelAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, MyReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmManager.cancel(alarmIntent)
    }
}