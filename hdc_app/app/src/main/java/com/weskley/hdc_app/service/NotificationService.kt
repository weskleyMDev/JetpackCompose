package com.weskley.hdc_app.service

import com.weskley.hdc_app.model.CustomNotification

interface NotificationService {
    fun createNotification(id: Int, title: String, body: String, imageUri: String)
    fun setRepeatingAlarm(item: CustomNotification)
    fun setDailyAlarm(item: CustomNotification)
    fun resetAlarm(item: CustomNotification)
    fun isAlarmActive(id: Int): Boolean
    fun cancelRepeatingAlarm(id: Int)
    fun cancelDailyAlarm(id: Int)
}