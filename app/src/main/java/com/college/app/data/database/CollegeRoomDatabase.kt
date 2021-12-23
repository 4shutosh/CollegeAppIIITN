package com.college.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.college.app.data.entities.TodoItem

@Database(
    entities = [
        TodoItem::class
    ],
    version = 3,
    exportSchema = false
)
abstract class CollegeRoomDatabase : RoomDatabase(), CollegeAppDatabase
// todo add type convertor here