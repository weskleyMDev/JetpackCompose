package com.weskley.hdc_app.constant

import android.app.NotificationManager
import androidx.core.app.NotificationCompat

object Constants {
    const val ICON_HOME = 0
    const val ICON_ALARM = 1
    const val ICON_PRESCRIPTION = 2
    const val ICON_PROFILE = 3

    const val CHANNEL_ID = "ch01"
    const val CHANNEL_NAME = "channel_name"
    const val CHANNEL_DESCRIPTION = "channel_description"
    const val HIGH_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
    const val HIGH_PRIORITY = NotificationCompat.PRIORITY_HIGH
    const val REQUEST_CODE = 1000
    const val REQUEST_CODE_ID = 0
    const val PENDING_CODE = 1
    const val GROUP_KEY = "hdc_group"
    const val SUMMARY_TITLE = "HDC"
    const val SUMMARY_TEXT = "Hora de Cuidar"
    const val SUMMARY_GROUP_TITLE = "HDC"
    const val SUMMARY_GROUP_TEXT = "Notificações perdidas"
}