package com.college.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.college.app.data.entities.TodoItem
import com.college.app.models.local.CollegeCourse

@Database(
    entities = [
        TodoItem::class, CollegeCourse::class
    ],
    version = 5,
    exportSchema = false
)
abstract class CollegeRoomDatabase : RoomDatabase(), CollegeAppDatabase
// todo add type convertor here