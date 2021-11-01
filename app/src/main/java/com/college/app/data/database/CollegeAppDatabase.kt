package com.college.app.data.database

import com.college.app.data.daos.TodoDao


interface CollegeAppDatabase {

    fun todoDao() : TodoDao

}