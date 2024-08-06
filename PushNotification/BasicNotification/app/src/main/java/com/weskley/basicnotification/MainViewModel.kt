package com.weskley.basicnotification

import android.app.NotificationManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var text: String by mutableStateOf("")

    fun showNotification() {
        notificationManager.notify(100, notificationBuilder.build())
    }

    fun createNotification(title: String, message: String) {
        notificationManager.notify(200, notificationService
            .createNotification(title, message).build())
    }
}