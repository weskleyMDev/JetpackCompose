package com.weskley.hdc_app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.weskley.hdc_app.event.MedicineEvent
import com.weskley.hdc_app.service.NotificationService
import com.weskley.hdc_app.viewmodel.MedicineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var service: NotificationService

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val name = intent.getStringExtra("name") ?: ""
            val amountStr = intent.getStringExtra("amount") ?: ""
            val type = intent.getStringExtra("type") ?: ""
            val time = intent.getStringExtra("time") ?: ""
            val image = intent.getStringExtra("image") ?: ""
            val id = intent.getIntExtra("id", 0)
            Log.d(
                "NotificationReceiver",
                "Received alarm: id=$id, name=$name, amount=$amountStr, type=$type, time=$time, image=$image"
            )
            if (::service.isInitialized) {
                service.createNotification(id, name, amountStr, type, image, time)
            } else {
                Log.e("NotificationReceiver", "NotificationService is not initialized.")
            }
        } catch (e: Exception) {
            Log.e("NotificationReceiver", "Error occurred while creating notification", e)
        }
    }
}