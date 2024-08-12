package com.weskley.hdc_app.module

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
    fun provideChannel(
        @ApplicationContext context: Context
    ): NotificationService {
        return NotificationServiceImp(context)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Notification