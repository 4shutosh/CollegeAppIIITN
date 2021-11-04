package com.college.app.data.repositories.todo

import com.college.app.data.daos.TodoDao
import com.college.base.logger.CollegeLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoDataSource @Inject constructor(
    private val todoDao: TodoDao,
    logger: CollegeLogger
) {



}