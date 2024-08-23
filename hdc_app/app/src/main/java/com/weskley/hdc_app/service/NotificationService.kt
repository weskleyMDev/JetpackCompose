package com.weskley.hdc_app.service

interface NotificationService {
    fun createNotification(id: Int, title: String, body: String, image: Int)
    fun setAlarm(id: Int, title: String, body: String, image: Int, hour: Int, minute: Int)
    fun cancelAlarm(id: Int)
}