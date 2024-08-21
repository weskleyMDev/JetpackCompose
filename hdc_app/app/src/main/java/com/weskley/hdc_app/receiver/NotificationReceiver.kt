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
            //if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
                val title = intent.getStringExtra("title") ?: ""
                val text = intent.getStringExtra("text") ?: ""
                val img = intent.getIntExtra("img", 0)
                service.createNotification(title, text, img)
            //}
        } catch (e: Exception) {
            Log.d("ERROR:", "${e.printStackTrace()}")
        }
    }
}