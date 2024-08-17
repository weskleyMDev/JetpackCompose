package com.weskley.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    val name: String?,
    val phone: String?,
    val email: String?,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)