package com.weskley.hdc_app.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notifications")
@Parcelize
data class CustomNotification(
    val title: String,
    val body: String,
    val time: String,
    val image: String,
    val active: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Parcelable