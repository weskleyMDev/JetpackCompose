package com.weskley.basicnotification.module

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.weskley.basicnotification.MainActivity
import com.weskley.basicnotification.R
import com.weskley.basicnotification.navigation.LNAME
import com.weskley.basicnotification.navigation.NAME
import com.weskley.basicnotification.navigation.URI
import com.weskley.basicnotification.receiver.MyReceiver
import com.weskley.basicnotification.service.NotificationService
import com.weskley.basicnotification.service.imp.NotificationServiceImp
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
const val HIGH_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
const val CHANNEL_ID2 = "channel_id2"
const val CHANNEL_NAME2 = "channel_name2"
const val CHANNEL_DESCRIPTION2 = "channel_description2"
const val LOW_IMPORTANCE = NotificationManager.IMPORTANCE_LOW

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    @Named("channel")
    fun provideNotificationChannel1(
        @ApplicationContext context: Context
    ): NotificationManager {
        val channel1 = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            HIGH_IMPORTANCE
        )
        val channel2 = NotificationChannel(
            CHANNEL_ID2,
            CHANNEL_NAME2,
            LOW_IMPORTANCE
        )
        channel1.description = CHANNEL_DESCRIPTION
        channel2.description = CHANNEL_DESCRIPTION2
        val notificationManager = context.getSystemService(Context
            .NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel1)
        notificationManager.createNotificationChannel(channel2)
        return notificationManager
    }

    @Provides
    @Singleton
    @Named("builder1")
    fun providesNotificationBuilder(
        @ApplicationContext context: Context,
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
//        val clickIntent = Intent(context, MainActivity::class.java)
//        val clickPendingIntent = PendingIntent.getActivity(
//            context,
//            1,
//            clickIntent,
//            PendingIntent.FLAG_IMMUTABLE
//        )
        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            "$URI/$NAME=NOTIFICATION&$LNAME=HILT".toUri(),
            context,
            MainActivity::class.java
        )
        val clickPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)
        }
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
    @Named("builder2")
    fun providesNotificationBuilder2(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID2)
            .setSmallIcon(R.drawable.baseline_cloud_download_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
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

// USE THIS ANNOTATIONS HERE AND MAINVIEWMODEL
//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class NotificationBuilder1
//
//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class NotificationBuilder2