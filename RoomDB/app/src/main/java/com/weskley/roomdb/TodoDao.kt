package com.weskley.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM Todo")
    fun findAllTodo(): Flow<List<Todo>>

    @Query("SELECT * FROM Todo WHERE id = :id")
    fun findTodoById(id: Int): Flow<Todo>

    @Upsert
    suspend fun upsertTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)
}