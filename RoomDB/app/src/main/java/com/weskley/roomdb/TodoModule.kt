package com.weskley.roomdb

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext applicationContext: Context
    ): TodoDataBase {
        return Room.databaseBuilder(
            applicationContext,
            TodoDataBase::class.java,
            TodoDataBase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDataBase): TodoDao {
        return database.getTodoDao()
    }
}