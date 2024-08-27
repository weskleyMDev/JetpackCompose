package com.weskley.hdc_app.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.weskley.hdc_app.model.CustomNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Upsert
    suspend fun upsertNotification(notification: CustomNotification)

    @Query("UPDATE CustomNotification SET active = :active WHERE id = :id")
    suspend fun updateActive(active: Boolean, id: Int)

    @Delete
    suspend fun deleteNotification(notification: CustomNotification)

    @Query("SELECT * FROM CustomNotification")
    fun findAll(): Flow<List<CustomNotification>>

    @Query("SELECT * FROM CustomNotification WHERE id = :id")
    fun findNotificationById(id: Int): Flow<CustomNotification?>

}