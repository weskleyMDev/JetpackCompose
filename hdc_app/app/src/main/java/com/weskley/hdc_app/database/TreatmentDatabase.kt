package com.weskley.hdc_app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weskley.hdc_app.dao.FeedbackDao
import com.weskley.hdc_app.dao.MedicineDao
import com.weskley.hdc_app.dao.NotificationDao
import com.weskley.hdc_app.dao.TreatmentDao
import com.weskley.hdc_app.dao.UserDao
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.model.Feedback
import com.weskley.hdc_app.model.Medicine
import com.weskley.hdc_app.model.Treatment
import com.weskley.hdc_app.model.User

@Database(
    entities = [
        Treatment::class,
        CustomNotification::class,
        User::class,
        Feedback::class,
        Medicine::class
    ],
    version = 1
)
abstract class TreatmentDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "hdc_db"
    }

    abstract fun treatmentDao(): TreatmentDao
    abstract fun notificationDao(): NotificationDao
    abstract fun userDao(): UserDao
    abstract fun feedbackDao(): FeedbackDao
    abstract fun medicineDao(): MedicineDao
}