package com.weskley.hdc_app.module

import android.app.AlarmManager
import android.content.Context
import androidx.room.Room
import com.weskley.hdc_app.dao.NotificationDao
import com.weskley.hdc_app.database.CustomNotificationDb
import com.weskley.hdc_app.service.NotificationService
import com.weskley.hdc_app.service.imp.NotificationServiceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    @Notification
    fun provideService(
        @ApplicationContext context: Context
    ): NotificationService {
        return NotificationServiceImp(context)
    }

    @Provides
    @Singleton
    @Manager
    fun provideAlarmManager(
        @ApplicationContext context: Context
    ): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CustomNotificationDb {
        return Room.databaseBuilder(
            context,
            CustomNotificationDb::class.java,
            CustomNotificationDb.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    @DatabaseDao
    fun provideNotificationDao(
        db: CustomNotificationDb
    ): NotificationDao {
        return db.notificationDao
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Notification

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Manager

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseDao