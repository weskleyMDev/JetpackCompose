package com.weskley.basicnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.weskley.basicnotification.receiver.MyReceiver
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
        val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra("message", "Notificacao com Hilt")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val clickIntent = Intent(context, MainActivity::class.java)
        val clickPendingIntent = PendingIntent.getActivity(
            context,
            1,
            clickIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Testando")
            .setContentText("Notificacao com Hilt")
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(0, "ACTION", pendingIntent)
            .setContentIntent(clickPendingIntent)
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