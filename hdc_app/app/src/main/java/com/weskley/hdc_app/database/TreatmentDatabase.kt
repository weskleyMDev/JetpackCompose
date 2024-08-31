package com.weskley.hdc_app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weskley.hdc_app.dao.TreatmentDao
import com.weskley.hdc_app.model.Treatment

@Database(entities = [Treatment::class], version = 1)
abstract class TreatmentDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "treatment_db"
    }

    abstract fun treatmentDao(): TreatmentDao
}