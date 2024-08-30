package com.weskley.hdc_app.module

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.weskley.hdc_app.dao.NotificationDao
import com.weskley.hdc_app.database.CustomNotificationDb
import com.weskley.hdc_app.service.NotificationService
import com.weskley.hdc_app.service.imp.NotificationServiceImp
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
    fun provideNotificationDao(
        db: CustomNotificationDb
    ): NotificationDao {
        return db.notificationDao
    }

    @Provides
    @Singleton
    fun providesFirebase(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}

/*@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseDao*/
