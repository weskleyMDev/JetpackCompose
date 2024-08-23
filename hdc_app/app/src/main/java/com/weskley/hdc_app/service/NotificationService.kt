package com.weskley.hdc_app.service

import com.weskley.hdc_app.model.CustomNotification

interface NotificationService {
    fun createNotification(id: Int, title: String, body: String, image: Int)
    fun setAlarm(item: CustomNotification)
    fun resetAlarm(item: CustomNotification)
    fun isAlarmActive(id: Int): Boolean
    fun cancelAlarm(id: Int)
}