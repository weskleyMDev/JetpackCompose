package com.weskley.hdc_app.module

import android.app.NotificationManager
import android.content.Context
import com.weskley.hdc_app.constant.Constants.CHANNEL_DESCRIPTION
import com.weskley.hdc_app.constant.Constants.CHANNEL_ID
import com.weskley.hdc_app.constant.Constants.CHANNEL_NAME
import com.weskley.hdc_app.constant.Constants.HIGH_IMPORTANCE
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
    @Channel
    fun provideChannel(
        @ApplicationContext context: Context
    ): NotificationManager {
        return NotificationServiceImp(context)
            .createChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                CHANNEL_DESCRIPTION,
                HIGH_IMPORTANCE
            )
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Notification

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Channel