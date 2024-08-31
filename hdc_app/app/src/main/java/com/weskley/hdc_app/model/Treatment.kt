package com.weskley.hdc_app.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "treatment")
@Parcelize
data class Treatment(
    val title: String,
    val startDate: String,
    val endDate: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable
