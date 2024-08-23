package com.weskley.hdc_app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.weskley.hdc_app.service.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var service: NotificationService

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val title = intent.getStringExtra("title") ?: ""
            val text = intent.getStringExtra("text") ?: ""
            val img = intent.getIntExtra("img", 0)
            val id = intent.getIntExtra("id", 0)

            Log.d("NotificationReceiver", "Received alarm: id=$id, title=$title, text=$text, img=$img")

            if (::service.isInitialized) {
                service.createNotification(id, title, text, img)
            } else {
                Log.e("NotificationReceiver", "NotificationService is not initialized.")
            }
        } catch (e: Exception) {
            Log.e("NotificationReceiver", "Error occurred while creating notification", e)
        }
    }
}