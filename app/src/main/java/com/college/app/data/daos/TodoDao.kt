package com.college.app.data.daos

import androidx.room.Query
import com.college.app.data.entities.TodoItem
import kotlinx.coroutines.flow.Flow

abstract class TodoDao : EntityDao<TodoItem>() {

    @Query("SELECT * FROM todos")
    abstract suspend fun getAllTodoFlow(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todos")
    abstract suspend fun getAllTodo(): List<TodoItem>

    @Query("SELECT * FROM todos WHERE isCompleted = :isCompleted")
    abstract suspend fun getAllIncompleteTodoFlow(isCompleted: Boolean = false): Flow<List<TodoItem>>

    @Query("SELECT * FROM todos WHERE id = :id")
    abstract suspend fun getTodoWithId(id: Long): TodoItem
}