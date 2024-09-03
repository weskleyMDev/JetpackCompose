package com.weskley.hdc_app.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.weskley.hdc_app.model.Treatment
import kotlinx.coroutines.flow.Flow

@Dao
interface TreatmentDao {

    @Upsert
    suspend fun upsertTreatment(treatment: Treatment)

    @Delete
    suspend fun deleteTreatment(treatment: Treatment)

    @Query("SELECT * FROM treatments ORDER BY id ASC")
    fun getTreatments(): Flow<List<Treatment>>

}