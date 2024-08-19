package com.weskley.hdc_app.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.weskley.hdc_app.model.CustomNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Upsert
    fun upsertNotification(notification: CustomNotification)

    @Query("DELETE FROM CustomNotification WHERE id = :id")
    fun deleteNotification(id: Int)

    @Query("SELECT * FROM CustomNotification")
    fun findAll(): Flow<List<CustomNotification>>

}