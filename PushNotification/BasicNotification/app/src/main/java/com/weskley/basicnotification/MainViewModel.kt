package com.weskley.basicnotification

import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.basicnotification.service.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named("builder1") private val notificationBuilder1: NotificationCompat.Builder,
    @Named("builder2") private val notificationBuilder2: NotificationCompat.Builder,
    @Named("channel") private val notificationManager: NotificationManager,
    @Named("custom") private val notificationService: NotificationService
): ViewModel() {

    var text: String by mutableStateOf("OLA")

    fun showNotification() {
        notificationManager.notify(100, notificationBuilder1.build())
    }

    fun showNotificationProgress() {
        val max = 10
        var progress = 0
        viewModelScope.launch {
            while (progress != max) {
                delay(1000)
                progress++
                notificationManager.notify(
                    300,
                    notificationBuilder2
                        .setContentTitle("Downloading")
                        .setContentText("${progress}/${max}")
                        .setProgress(max, progress, false)
                        .build())
            }
            notificationManager.notify(
                300,
                notificationBuilder1
                    .setContentTitle("Download Complete")
                    .setContentText("")
                    .setContentIntent(null)
                    .clearActions()
                    .setProgress(0,0,false)
                    .build())
        }
    }

    fun createNotification(context: Context, title: String, message: String) {
        notificationManager.notify(200, notificationService
            .createNotification(context, title, message).build())
    }
}