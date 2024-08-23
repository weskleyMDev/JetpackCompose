package com.weskley.hdc_app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.weskley.hdc_app.model.CustomNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: CustomNotification)

    @Update
    suspend fun updateNotification(notification: CustomNotification)

    @Transaction
    suspend fun insertOrUpdateNotification(notification: CustomNotification) {
        if (notification.id == 0) {
            insertNotification(notification)
        } else {
            updateNotification(notification)
        }
    }

    @Query("UPDATE CustomNotification SET active = :active WHERE id = :id")
    suspend fun updateActive(active: Boolean, id: Int)

    @Query("DELETE FROM CustomNotification WHERE id = :id")
    suspend fun deleteNotification(id: Int)

    @Query("SELECT * FROM CustomNotification")
    fun findAll(): Flow<List<CustomNotification>>

}