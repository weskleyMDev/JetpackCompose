package com.weskley.hdc_app.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.weskley.hdc_app.model.Medicine
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Upsert
    suspend fun upsertMedicine(medicine: Medicine)

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("SELECT * FROM medicines")
    fun getMedicines(): Flow<List<Medicine>>

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicineById(id: Int): Medicine?

    @Query("UPDATE medicines SET active = :active WHERE id = :id")
    suspend fun updateActiveStatus(active: Boolean, id: Int)
}