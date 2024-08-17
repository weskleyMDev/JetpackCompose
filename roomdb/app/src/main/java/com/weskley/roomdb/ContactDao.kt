package com.weskley.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Upsert
    fun upsertContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contact ORDER BY name ASC")
    fun getContactOrderByName(): Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY email ASC")
    fun getContactOrderByEmail(): Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY phone ASC")
    fun getContactOrderByPhone(): Flow<List<Contact>>
}