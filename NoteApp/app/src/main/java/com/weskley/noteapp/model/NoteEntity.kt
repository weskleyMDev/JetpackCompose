package com.weskley.noteapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class NoteEntity(
    val title: String,
    val content: String,
    val time: String,
    val isActive: Boolean,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
): Parcelable
