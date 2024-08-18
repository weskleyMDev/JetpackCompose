package com.weskley.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM Todo")
    fun getAllTodo(): Flow<List<Todo>>

    @Upsert
    fun addTodo(todo: Todo)

    @Delete
    fun deleteTodo(todo: Todo)
}