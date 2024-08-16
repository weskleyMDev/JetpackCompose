package com.weskley.hdc_app.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.weskley.hdc_app.service.imp.NotificationServiceImp
import com.weskley.hdc_app.viewmodel.AlarmViewModel

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val title = intent.getStringExtra("title") ?: ""
            val text = intent.getStringExtra("text") ?: ""
            val img = intent.getIntExtra("img", 0)
            val service = AlarmViewModel(
                NotificationServiceImp(context),
                notificationManager
            )
            service.showNotification(context, title, text, img)
        } catch (e: Exception) {
            Log.d("ERROR:","${e.printStackTrace()}")
        }
    }
}