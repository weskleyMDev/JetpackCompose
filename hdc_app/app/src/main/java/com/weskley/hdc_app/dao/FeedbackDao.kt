package com.weskley.hdc_app.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.weskley.hdc_app.model.Feedback
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDao {

    @Upsert
    suspend fun upsertFeedback(feedback: Feedback)

    @Delete
    suspend fun deleteFeedback(feedback: Feedback)

    @Query("SELECT * FROM feedbacks")
    fun getAllFeedbacks(): Flow<List<Feedback>>
}