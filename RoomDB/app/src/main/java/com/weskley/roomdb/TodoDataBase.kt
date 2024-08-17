package com.weskley.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 1)
abstract class TodoDataBase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "todo_db"
    }

    abstract fun getTodoDao(): TodoDao
}