package com.weskley.hdc_app.viewmodel

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.weskley.hdc_app.R
import com.weskley.hdc_app.constant.Constants
import com.weskley.hdc_app.module.Notification
import com.weskley.hdc_app.service.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    @Notification private val service: NotificationService
): ViewModel() {
    var selected = mutableIntStateOf(0)
    private val myImage by mutableIntStateOf(R.drawable.paracetamol)

    fun showNotification(context: Context) {
        service.createChannel(
            Constants.CHANNEL_ID,
            Constants.CHANNEL_NAME,
            Constants.CHANNEL_DESCRIPTION,
            Constants.HIGH_IMPORTANCE
        ).notify(
            Constants.REQUEST_CODE,
            service.createNotification(
                Constants.CHANNEL_ID,
                R.drawable.logo_circ_branco,
                "Paracetamol 100 mg",
                "Tomar 2 doses em jejum",
                Constants.HIGH_PRIORITY,
                selectImage(context.resources, myImage),
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(selectImage(context.resources, myImage))
                    .bigLargeIcon(null as Bitmap?)
            ).build()
        )
    }
}

private fun selectImage(resource: Resources, image: Int): Bitmap {
    return BitmapFactory.decodeResource(resource, image)
}