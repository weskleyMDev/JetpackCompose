package com.weskley.hdc_app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weskley.hdc_app.dao.NotificationDao
import com.weskley.hdc_app.model.CustomNotification

@Database(entities = [CustomNotification::class], version = 1)
abstract class CustomNotificationDb: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "notification_db"
    }

    abstract val notificationDao: NotificationDao
}