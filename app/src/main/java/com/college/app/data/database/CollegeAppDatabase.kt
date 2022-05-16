package com.college.app.data.database

import com.college.app.data.daos.AnnouncementsDao
import com.college.app.data.daos.CoursesDao
import com.college.app.data.daos.TodoDao


interface CollegeAppDatabase {

    fun todoDao() : TodoDao

    fun coursesDao(): CoursesDao

    fun announcementsDao(): AnnouncementsDao
}