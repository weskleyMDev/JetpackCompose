package com.weskley.hdc_app.service

import com.weskley.hdc_app.model.Medicine

interface NotificationService {
    fun createNotification(id: Int, name: String, amount: String, type: String,
                           imagePath: String, time: String)
    fun setDailyAlarm(medicine: Medicine)
    fun setRepeatingAlarm(medicine: Medicine)
    fun isAlarmActive(id: Int): Boolean
    fun cancelAlarm(id: Int)
}