package com.weskley.hdc_app.module

import android.content.Context
import androidx.room.Room
import com.weskley.hdc_app.dao.TreatmentDao
import com.weskley.hdc_app.database.TreatmentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TreatmentModule {

    @Provides
    @Singleton
    fun provideTreatmentDatabase(
        @ApplicationContext context: Context
    ): TreatmentDatabase {
        return Room.databaseBuilder(
            context,
            TreatmentDatabase::class.java,
            TreatmentDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTreatmentDao(
        treatmentDatabase: TreatmentDatabase
    ): TreatmentDao {
        return treatmentDatabase.treatmentDao()
    }
}