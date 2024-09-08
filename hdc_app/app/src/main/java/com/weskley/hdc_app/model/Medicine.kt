package com.weskley.hdc_app.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "medicines")
@Parcelize
data class Medicine(
    val name: String,
    val amount: String,
    val type: String,
    val time: String,
    val image: String,
    val repetition: String,
    val count: Int,
    val active: Boolean,
    val treatmentId: Int?,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Parcelable
