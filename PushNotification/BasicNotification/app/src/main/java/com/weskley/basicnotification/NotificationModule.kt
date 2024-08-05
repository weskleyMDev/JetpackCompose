package com.weskley.basicnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

const val CHANNEL_ID = "channel_id"
const val CHANNEL_NAME = "channel_name"
const val CHANNEL_DESCRIPTION = "channel_description"
const val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    @Named("manager")
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            CHANNEL_IMPORTANCE
        )
        notificationChannel.description = CHANNEL_DESCRIPTION
        val notificationManager = context.getSystemService(Context
            .NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        return notificationManager
    }

    @Provides
    @Singleton
    @Named("builder")
    fun providesNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Testando")
            .setContentText("Notificacao com Hilt")
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        return notification
    }

    @Provides
    @Singleton
    @Named("custom")
    fun createNotificationService(
        @ApplicationContext context: Context
    ): NotificationService {
        return NotificationServiceImp(context)
    }
}