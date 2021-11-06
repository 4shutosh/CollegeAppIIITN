package com.college.app.data.repositories.todo

import com.college.app.data.daos.TodoDao
import com.college.app.data.entities.TodoItem
import com.college.base.logger.CollegeLogger
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoDataSource @Inject constructor(
    private val todoDao: TodoDao,
    logger: CollegeLogger
) {

    suspend fun getAllTodo(): Flow<List<TodoItem>> = todoDao.getAllTodoFlow()

    suspend fun getAllIncompleteTodo(): Flow<List<TodoItem>> = todoDao.getAllIncompleteTodoFlow()

    suspend fun getTodoWithId(id: Long) = todoDao.getTodoWithId(id)

}