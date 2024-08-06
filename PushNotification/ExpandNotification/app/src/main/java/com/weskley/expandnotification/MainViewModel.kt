package com.weskley.expandnotification

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import com.weskley.expandnotification.constants.ApplicationConstants
import com.weskley.expandnotification.service.imp.ApplicationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named("channel") private val channel: ApplicationService,
    @Named("notification") private val notification: ApplicationService
): ViewModel() {
    var title: String by mutableStateOf("")
    var message: String by mutableStateOf("")
    val focus = FocusRequester()

    fun showSimpleNotification(context: Context) {
        channel.createChannel(
            ApplicationConstants.CHANNEL_ID,
            ApplicationConstants.CHANNEL_NAME,
            ApplicationConstants.CHANNEL_DESCRIPTION,
            ApplicationConstants.HIGH_IMPORTANCE
        ).notify(
            1,
            notification.createNotification(
                ApplicationConstants.CHANNEL_ID,
                R.drawable.baseline_circle_notifications_24,
                title.trim().replaceFirstChar { value ->
                    value.uppercase()
                },
                message.trim().replaceFirstChar { value ->
                    value.uppercase()
                },
                ApplicationConstants.HIGH_PRIORITY,
                BitmapFactory.decodeResource(context.resources, R.drawable.icon)
            ).build()
        )
    }
}