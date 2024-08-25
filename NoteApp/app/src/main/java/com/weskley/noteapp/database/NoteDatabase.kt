package com.weskley.noteapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weskley.noteapp.model.NoteEntity
import com.weskley.noteapp.service.NoteDao

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "note_db"
    }

    abstract fun noteDao(): NoteDao
}