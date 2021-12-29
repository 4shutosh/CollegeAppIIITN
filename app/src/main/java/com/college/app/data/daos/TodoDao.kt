package com.college.app.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.college.app.data.entities.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TodoDao : EntityDao<TodoItem>() {

    @Query("SELECT * FROM todos")
    abstract fun getAllTodoFlow(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todos")
    abstract suspend fun getAllTodo(): List<TodoItem>

    @Query("SELECT * FROM todos WHERE isCompleted = :isCompleted")
    abstract fun getAllIncompleteTodoFlow(isCompleted: Boolean = false): Flow<List<TodoItem>>

    @Query("SELECT * FROM todos WHERE id = :id")
    abstract suspend fun getTodoWithId(id: Long): TodoItem

    @Query("DELETE FROM todos WHERE id = :id")
    abstract suspend fun deleteById(id: Long)
}