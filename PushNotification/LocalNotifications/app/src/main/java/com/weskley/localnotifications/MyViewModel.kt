package com.weskley.localnotifications

import android.content.Context
import androidx.lifecycle.ViewModel

class MyViewModel(private val context: Context): ViewModel() {

    private val service = NotificationService(context)

    fun showNotification(title: String, message: String) {
        service.showSimpleNotification(title, message)
    }
}