package com.college.app.data.repositories.todo

import com.college.app.data.daos.TodoDao
import com.college.app.data.entities.TodoItem
import com.college.base.logger.CollegeLogger
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao,
    logger: CollegeLogger
) {

    suspend fun insertTodo(todo: TodoItem) = todoDao.insert(todo)

    suspend fun deleteTodo(id: Long) = todoDao.deleteById(id)

    fun getAllTodoFlow(): Flow<List<TodoItem>> = todoDao.getAllTodoFlow()

    fun getAllTodoFlowSorted() = todoDao.getAllTodoTimestampSortedFlow()

    suspend fun getAllTodo(): List<TodoItem> = todoDao.getAllTodo()

    suspend fun getAllIncompleteTodo(): Flow<List<TodoItem>> = todoDao.getAllIncompleteTodoFlow()

    suspend fun getTodoWithId(id: Long) = todoDao.getTodoWithId(id)

}