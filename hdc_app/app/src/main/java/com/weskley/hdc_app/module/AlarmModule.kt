package com.weskley.hdc_app.module

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.service.NotificationService
import com.weskley.hdc_app.service.imp.NotificationServiceImp
import com.weskley.hdc_app.viewmodel.MedicineViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmManager(
        @ApplicationContext context: Context
    ): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context,
    ) : NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideService(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager,
        notificationManager: NotificationManager
    ): NotificationService {
        return NotificationServiceImp(context, alarmManager, notificationManager)
    }
}

/*@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseDao*/
