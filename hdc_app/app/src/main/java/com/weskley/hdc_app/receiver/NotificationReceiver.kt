package com.weskley.hdc_app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.weskley.hdc_app.event.MedicineEvent
import com.weskley.hdc_app.model.Medicine
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
            val medicineJson = intent.getStringExtra("medicineJson")
            val gson = Gson()
            val medicine: Medicine? = gson.fromJson(medicineJson, Medicine::class.java)
            if (medicine != null) {
                val id = medicine.id
                val name = medicine.name
                val amountStr = medicine.amount
                val type = medicine.type
                val time = medicine.time
                val image = medicine.image

                Log.d(
                    "NotificationReceiver",
                    "Received alarm: id=$id, name=$name, amount=$amountStr, type=$type, time=$time, image=$image"
                )
                if (::service.isInitialized) {
                    service.createNotification(id, name, amountStr, type, image, time)
                    service.cancelAlarm(id)
                    service.setRepeatingAlarm(medicine)
                } else {
                    Log.e("NotificationReceiver", "NotificationService is not initialized.")
                }
            } else {
                Log.e("NotificationReceiver", "No Medicine data found in the intent.")
            }
        } catch (e: Exception) {
            Log.e("NotificationReceiver", "Error occurred while creating notification", e)
        }
    }
}