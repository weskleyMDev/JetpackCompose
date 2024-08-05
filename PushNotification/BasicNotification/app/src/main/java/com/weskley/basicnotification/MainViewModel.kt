package com.weskley.basicnotification

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named("builder") private val notificationBuilder: NotificationCompat.Builder,
    @Named("manager") private val notificationManager: NotificationManager,
    @Named("custom") private val notificationService: NotificationService
): ViewModel() {
    fun showNotification() {
        notificationManager.notify(100, notificationBuilder.build())
    }

    fun createNotification(title: String, message: String) {
        notificationManager.notify(200, notificationService
            .createNotification(title, message).build())
    }
}