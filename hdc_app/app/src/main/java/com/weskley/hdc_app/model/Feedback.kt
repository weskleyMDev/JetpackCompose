package com.weskley.hdc_app.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "feedbacks")
@Parcelize
data class Feedback(
    val medicine: String,
    val answer: String,
    val justification: String,
    val entryTime: String,
    val shippingTime: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Parcelable
