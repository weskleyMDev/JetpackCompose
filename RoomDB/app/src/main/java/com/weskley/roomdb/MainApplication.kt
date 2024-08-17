package com.weskley.roomdb

import android.app.Application
import androidx.room.Room

class MainApplication: Application() {

    companion object {
        lateinit var todoDataBase: TodoDataBase
    }

    override fun onCreate() {
        super.onCreate()
        todoDataBase = Room.databaseBuilder(
            applicationContext,
            TodoDataBase::class.java,
            TodoDataBase.DATABASE_NAME
        ).build()
    }
}