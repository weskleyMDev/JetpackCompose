package com.weskley.hdc_app.module

import android.app.AlarmManager
import android.content.Context
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
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Notification

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Manager