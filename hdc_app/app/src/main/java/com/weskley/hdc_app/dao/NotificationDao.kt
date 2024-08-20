package com.weskley.hdc_app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weskley.hdc_app.model.CustomNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notification: CustomNotification)

    @Query("UPDATE CustomNotification SET active = :active WHERE id = :id")
    fun updateActive(active: Boolean, id: Int)

    @Query("DELETE FROM CustomNotification WHERE id = :id")
    fun deleteNotification(id: Int)

    @Query("SELECT * FROM CustomNotification")
    fun findAll(): Flow<List<CustomNotification>>

}