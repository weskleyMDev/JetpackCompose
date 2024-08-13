package com.weskley.hdc_app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
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