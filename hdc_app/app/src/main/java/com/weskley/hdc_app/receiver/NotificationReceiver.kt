package com.weskley.hdc_app.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.weskley.hdc_app.model.InputModel
import com.weskley.hdc_app.service.NotificationService
import com.weskley.hdc_app.service.imp.NotificationServiceImp
import com.weskley.hdc_app.viewmodel.AlarmViewModel

class NotificationReceiver: BroadcastReceiver() {
    private lateinit var manager: NotificationManager
    private lateinit var service: NotificationService
    private lateinit var input: InputModel
    override fun onReceive(context: Context, intent: Intent) {
        try {
            manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service = NotificationServiceImp(context)
            input = InputModel()
            val title = intent.getStringExtra("title") ?: ""
            val text = intent.getStringExtra("text") ?: ""
            val img = intent.getIntExtra("img", 0)
            val viewModel = AlarmViewModel(
                service,
                manager,
                input
            )
            viewModel.showNotification(context, title, text, img)
        } catch (e: Exception) {
            Log.d("ERROR:","${e.printStackTrace()}")
        }
    }
}