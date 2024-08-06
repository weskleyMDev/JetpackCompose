package com.weskley.expandnotification.module

import android.content.Context
import com.weskley.expandnotification.service.imp.ApplicationService
import com.weskley.expandnotification.service.imp.ApplicationServiceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    @Named("channel")
    fun provideChannel(
        @ApplicationContext context: Context
    ): ApplicationService {
        return ApplicationServiceImp(context)
    }

    @Provides
    @Singleton
    @Named("notification")
    fun provideNotification(
        @ApplicationContext context: Context
    ): ApplicationService {
        return ApplicationServiceImp(context)
    }
}