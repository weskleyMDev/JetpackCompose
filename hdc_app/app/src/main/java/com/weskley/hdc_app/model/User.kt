package com.weskley.hdc_app.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
data class User(
    val name: String,
    val age: Int,
    val bloodType: String,
    val imageUri: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Parcelable
